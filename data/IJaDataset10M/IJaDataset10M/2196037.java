package com.l2jserver.gameserver.model;

import com.l2jserver.Config;
import com.l2jserver.util.Rnd;
import javolution.util.FastList;

/**
 *
 * @author  Fulminus
 */
public class L2DropCategory {

    private FastList<L2DropData> _drops;

    private int _categoryChance;

    private int _categoryBalancedChance;

    private int _categoryType;

    public L2DropCategory(int categoryType) {
        _categoryType = categoryType;
        _drops = new FastList<L2DropData>(0);
        _categoryChance = 0;
        _categoryBalancedChance = 0;
    }

    public void addDropData(L2DropData drop, boolean raid) {
        boolean found = false;
        if (drop.isQuestDrop()) {
        } else {
            if (Config.CUSTOM_DROPLIST_TABLE) {
                for (L2DropData d : _drops) {
                    if (d.getItemId() == drop.getItemId()) {
                        d.setMinDrop(drop.getMinDrop());
                        d.setMaxDrop(drop.getMaxDrop());
                        if (d.getChance() != drop.getChance()) {
                            _categoryChance -= d.getChance();
                            _categoryBalancedChance -= Math.min((d.getChance() * Config.RATE_DROP_ITEMS), L2DropData.MAX_CHANCE);
                            d.setChance(drop.getChance());
                            _categoryChance += d.getChance();
                            _categoryBalancedChance += Math.min((d.getChance() * Config.RATE_DROP_ITEMS), L2DropData.MAX_CHANCE);
                        }
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                _drops.add(drop);
                _categoryChance += drop.getChance();
                _categoryBalancedChance += Math.min((drop.getChance() * (raid ? Config.RATE_DROP_ITEMS_BY_RAID : Config.RATE_DROP_ITEMS)), L2DropData.MAX_CHANCE);
            }
        }
    }

    public FastList<L2DropData> getAllDrops() {
        return _drops;
    }

    public void clearAllDrops() {
        _drops.clear();
    }

    public boolean isSweep() {
        return (getCategoryType() == -1);
    }

    public int getCategoryChance() {
        if (getCategoryType() >= 0) return _categoryChance; else return L2DropData.MAX_CHANCE;
    }

    public int getCategoryBalancedChance() {
        if (getCategoryType() >= 0) return _categoryBalancedChance; else return L2DropData.MAX_CHANCE;
    }

    public int getCategoryType() {
        return _categoryType;
    }

    /**
     * useful for seeded conditions...the category will attempt to drop only among
     * items that are allowed to be dropped when a mob is seeded.
     * Previously, this only included adena.  According to sh1ny, sealstones are also
     * acceptable drops.
     * if no acceptable drops are in the category, nothing will be dropped.
     * otherwise, it will check for the item's chance to drop and either drop
     * it or drop nothing.
     *
     * @return acceptable drop when mob is seeded, if it exists.  Null otherwise.
     */
    public synchronized L2DropData dropSeedAllowedDropsOnly() {
        FastList<L2DropData> drops = new FastList<L2DropData>();
        int subCatChance = 0;
        for (L2DropData drop : getAllDrops()) {
            if ((drop.getItemId() == 57) || (drop.getItemId() == 6360) || (drop.getItemId() == 6361) || (drop.getItemId() == 6362)) {
                drops.add(drop);
                subCatChance += drop.getChance();
            }
        }
        int randomIndex = Rnd.get(subCatChance);
        int sum = 0;
        for (L2DropData drop : drops) {
            sum += drop.getChance();
            if (sum > randomIndex) {
                drops.clear();
                drops = null;
                return drop;
            }
        }
        return null;
    }

    /**
 * ONE of the drops in this category is to be dropped now.
 * to see which one will be dropped, weight all items' chances such that
 * their sum of chances equals MAX_CHANCE.
 * since the individual drops have their base chance, we also ought to use the
 * base category chance for the weight.  So weight = MAX_CHANCE/basecategoryDropChance.
 * Then get a single random number within this range.  The first item
 * (in order of the list) whose contribution to the sum makes the
 * sum greater than the random number, will be dropped.
 *
 * Edited: How _categoryBalancedChance works in high rate servers:
 * Let's say item1 has a drop chance (when considered alone, without category) of
 * 1 % * RATE_DROP_ITEMS and item2 has 20 % * RATE_DROP_ITEMS, and the server's
 * RATE_DROP_ITEMS is for example 50x. Without this balancer, the relative chance inside
 * the category to select item1 to be dropped would be 1/26 and item2 25/26, no matter
 * what rates are used. In high rate servers people usually consider the 1 % individual
 * drop chance should become higher than this relative chance (1/26) inside the category,
 * since having the both items for example in their own categories would result in having
 * a drop chance for item1 50 % and item2 1000 %. _categoryBalancedChance limits the
 * individual chances to 100 % max, making the chance for item1 to be selected from this
 * category 50/(50+100) = 1/3 and item2 100/150 = 2/3.
 * This change doesn't affect calculation when drop_chance * RATE_DROP_ITEMS < 100 %,
 * meaning there are no big changes for low rate servers and no changes at all for 1x
 * servers.
 *
 * @return selected drop from category, or null if nothing is dropped.
 */
    public synchronized L2DropData dropOne(boolean raid) {
        int randomIndex = Rnd.get(getCategoryBalancedChance());
        int sum = 0;
        for (L2DropData drop : getAllDrops()) {
            sum += Math.min((drop.getChance() * (raid ? Config.RATE_DROP_ITEMS_BY_RAID : Config.RATE_DROP_ITEMS)), L2DropData.MAX_CHANCE);
            if (sum >= randomIndex) return drop;
        }
        return null;
    }
}
