package org.dizem.sanguosha.model.player;

import org.dizem.sanguosha.model.card.AbstractCard;
import org.dizem.sanguosha.model.card.character.Character;
import org.dizem.sanguosha.model.card.equipment.EquipmentCard;
import org.dizem.sanguosha.model.exception.SGSException;
import org.dizem.sanguosha.model.vo.PlayerVO;
import java.util.ArrayList;
import java.util.List;

/**
 * User: DIZEM
 * Time: 11-3-30 下午3:10
 */
public class Player {

    private String ip;

    private int port;

    private int playerId;

    private String name;

    private Role role;

    private Phase phase;

    private org.dizem.sanguosha.model.card.character.Character character;

    private List<AbstractCard> handCards;

    private List<EquipmentCard> equipmentCards;

    private List<AbstractCard> effectCards;

    private static List<Player> instance;

    public Player(String name) {
        this.role = role;
        this.name = name;
        effectCards = new ArrayList<AbstractCard>();
        handCards = new ArrayList<AbstractCard>();
        equipmentCards = new ArrayList<EquipmentCard>();
        this.phase = Phase.START;
    }

    public Player(String name, String ip, int port) {
        this(name);
        this.ip = ip;
        this.port = port;
    }

    public Player(PlayerVO playerVO) {
        this.ip = playerVO.getIp();
        this.port = playerVO.getPort();
        this.name = playerVO.getName();
        this.playerId = playerVO.getPlayerId();
        effectCards = new ArrayList<AbstractCard>();
        handCards = new ArrayList<AbstractCard>();
        equipmentCards = new ArrayList<EquipmentCard>();
        this.phase = Phase.NOT_ACTIVE;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public static synchronized List<Player> getPlayerList() {
        if (instance == null) instance = new ArrayList<Player>();
        return instance;
    }

    /**
	 * 是否有空位装备当前装备牌
	 *
	 * @param card 装备牌
	 * @return 能否装备
	 */
    public boolean canAddEquipmentCard(EquipmentCard card) {
        for (EquipmentCard equipmentCard : equipmentCards) {
            if (equipmentCard.getCardType() == card.getCardType()) {
                System.out.println(getCharacter().getName() + "+++" + card.getName());
                return false;
            }
        }
        return true;
    }

    public void addEquipmentCard(EquipmentCard card) {
        if (!canAddEquipmentCard(card)) {
            throw new SGSException("Player has already had this type of equipmentCard");
        } else {
            System.out.println(getCharacter().getName() + "装备了" + card.getName());
            equipmentCards.add(card);
        }
    }

    public String getCharacterName() {
        return character.getName();
    }

    public String getName() {
        return name;
    }

    public void addHandCard(AbstractCard card) {
        handCards.add(card);
    }

    public void removeHandCard(AbstractCard card) {
        handCards.remove(card);
    }

    public List<AbstractCard> getHandCards() {
        return handCards;
    }

    public List<AbstractCard> getEffectCards() {
        return effectCards;
    }

    public org.dizem.sanguosha.model.card.character.Character getCharacter() {
        return character;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getRoleID() {
        switch(role) {
            case ZG:
                return 0;
            case ZC:
                return 1;
            case NJ:
                return 3;
            default:
                return 2;
        }
    }

    public int getLife() {
        return character.getLife();
    }

    public int cardToBeDiscard() {
        System.out.println("cardToBeDiscard: " + handCards.size() + " " + character.getLife());
        return handCards.size() - character.getLife();
    }

    public void removeEffectCard(AbstractCard card) {
        effectCards.remove(card);
    }

    public Phase getPhase() {
        return phase;
    }

    public int getPhaseID() {
        switch(phase) {
            case START:
                return 0;
            case JUDGE:
                return 1;
            case DRAW:
                return 2;
            case PLAY:
                return 3;
            case DISCARD:
                return 4;
            case FINISH:
                return 5;
            default:
                return 6;
        }
    }

    public void setPhase(Phase phase) {
        System.out.println(getCharacter().getName() + "改变阶段：" + phase);
        this.phase = phase;
    }

    public EquipmentCard getEquipmentCard(int type) {
        for (EquipmentCard card : equipmentCards) {
            if (card.getCardType() == type) {
                return card;
            }
        }
        return null;
    }

    public void removeEquipmentCard(int type) {
        for (EquipmentCard card : equipmentCards) {
            if (card.getCardType() == type) {
                equipmentCards.remove(card);
                break;
            }
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean needToDiscard() {
        System.out.println("life = " + character.getLife() + "  " + handCards.size());
        return character.getLife() < handCards.size();
    }
}
