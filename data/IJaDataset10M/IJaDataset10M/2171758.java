package org.nicocube.airain.domain;

import java.util.List;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nicocube.airain.domain.client.character.Action;
import org.nicocube.airain.domain.client.character.ActionType;
import org.nicocube.airain.domain.client.data.StorageException;
import org.nicocube.airain.domain.client.gamedate.GameDate;
import org.nicocube.airain.domain.server.criteria.ConstraintCriteria;
import org.nicocube.airain.domain.server.data.ContainerProviderImpl;
import org.nicocube.airain.domain.server.data.OrderedStorage;
import org.nicocube.airain.domain.server.data.StorageProvider;
import org.nicocube.airain.ds.CommonDSTest;
import org.nicocube.airain.utils.config.Config;
import org.nicocube.airain.utils.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionTest extends CommonDSTest {

    private static final Logger log = LoggerFactory.getLogger(ActionTest.class);

    private static final int NB_INSERT = 100;

    private static final int NB_GAME_CHARACTER = 10;

    private static final int NB_RETRIEVE = 10;

    private static StorageProvider storageProvider;

    @BeforeClass
    public static void prepare() throws Exception {
        Config c = ConfigProvider.inst().provide("storage.properties");
        ContainerProviderImpl cp = new ContainerProviderImpl(c);
        storageProvider = new StorageProvider(cp);
    }

    @Test
    public void massInsert() throws StorageException {
        OrderedStorage<Action> db = storageProvider.getStorage(Action.class);
        ActionType[] type = ActionType.values();
        long max = 0;
        long min = 0;
        long sum = 0;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < NB_INSERT; i++) {
            Random r = new Random();
            long gcO = r.nextInt(NB_GAME_CHARACTER) + 1;
            int h = r.nextInt(24) + 1;
            int d = r.nextInt(28) + 1;
            int m = r.nextInt(12) + 1;
            int y = 1;
            GameDate g = new GameDate(h, d, m, y);
            long inter_begin = System.currentTimeMillis();
            db.save(Action.build(type[r.nextInt(type.length)], gcO, g));
            long inter_end = System.currentTimeMillis();
            max = Math.max(max, inter_end - inter_begin);
            min = min == 0 ? inter_end - inter_begin : Math.min(min, inter_end - inter_begin);
            sum += (inter_end - inter_begin);
        }
        long end = System.currentTimeMillis();
        log.info("\n" + "Mass Insert of " + NB_INSERT + " elements\n" + "total=" + (end - begin) + "ms\n" + "sum=" + sum + "ms\n" + "max=" + max + "ms\n" + "min=" + min + "ms\n" + "moy=" + (((float) sum) / NB_INSERT) + "ms\n");
        db.commit();
    }

    @Test
    public void massInsertCommit() throws StorageException {
        OrderedStorage<Action> db = storageProvider.getStorage(Action.class);
        ActionType[] type = ActionType.values();
        long max = 0;
        long min = 0;
        long sum = 0;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < NB_INSERT; i++) {
            Random r = new Random();
            long gcO = r.nextInt(NB_GAME_CHARACTER) + 1;
            int h = r.nextInt(24) + 1;
            int d = r.nextInt(28) + 1;
            int m = r.nextInt(12) + 1;
            int y = 1;
            GameDate g = new GameDate(h, d, m, y);
            long inter_begin = System.currentTimeMillis();
            db.save(Action.build(type[r.nextInt(type.length)], gcO, g));
            if (i % (NB_INSERT / 10) == 0) db.commit();
            long inter_end = System.currentTimeMillis();
            max = Math.max(max, inter_end - inter_begin);
            min = min == 0 ? inter_end - inter_begin : Math.min(min, inter_end - inter_begin);
            sum += (inter_end - inter_begin);
        }
        long end = System.currentTimeMillis();
        log.info("\n" + "Mass Insert of " + NB_INSERT + " elements, commit each 10%\n" + "total=" + (end - begin) + "ms\n" + "sum=" + sum + "ms\n" + "max=" + max + "ms\n" + "min=" + min + "ms\n" + "moy=" + (((float) sum) / NB_INSERT) + "ms\n");
        db.commit();
    }

    @Test
    public void randomRetrieve() throws StorageException {
        OrderedStorage<Action> db = storageProvider.getStorage(Action.class);
        long max = 0;
        long min = 0;
        long sum = 0;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < (NB_RETRIEVE); i++) {
            Random r = new Random();
            long order = r.nextInt(NB_INSERT) + 1;
            long inter_begin = System.currentTimeMillis();
            db.retrieveByOrder(order);
            long inter_end = System.currentTimeMillis();
            max = Math.max(max, inter_end - inter_begin);
            min = min == 0 ? inter_end - inter_begin : Math.min(min, inter_end - inter_begin);
            sum += (inter_end - inter_begin);
        }
        long end = System.currentTimeMillis();
        log.info("\n" + NB_RETRIEVE + " random unique retrieve in " + NB_INSERT + " elements\n" + "total=" + (end - begin) + "ms\n" + "sum=" + sum + "ms\n" + "max=" + max + "ms\n" + "min=" + min + "ms\n" + "moy=" + (((float) sum) / NB_RETRIEVE) + "ms\n");
        db.commit();
    }

    @Test
    public void retrieveByGameCharacter() throws StorageException {
        OrderedStorage<Action> db = storageProvider.getStorage(Action.class);
        long max = 0;
        long min = 0;
        long sum = 0;
        long begin = System.currentTimeMillis();
        for (long i = 0; i < (NB_GAME_CHARACTER); i++) {
            long inter_begin = System.currentTimeMillis();
            db.retrieve(new ConstraintCriteria<Long>("gameCharacterOrder", i));
            long inter_end = System.currentTimeMillis();
            max = Math.max(max, inter_end - inter_begin);
            min = min == 0 ? inter_end - inter_begin : Math.min(min, inter_end - inter_begin);
            sum += (inter_end - inter_begin);
        }
        long end = System.currentTimeMillis();
        log.info("\n" + NB_GAME_CHARACTER + " random List retrieve in " + NB_INSERT + " elements\n" + "total=" + (end - begin) + "ms\n" + "sum=" + sum + "ms\n" + "max=" + max + "ms\n" + "min=" + min + "ms\n" + "moy=" + (((float) sum) / NB_GAME_CHARACTER) + "ms\n");
        db.commit();
    }

    @Test
    public void retrieveByGameCharacterAndModify() throws StorageException {
        OrderedStorage<Action> db = storageProvider.getStorage(Action.class);
        long max = 0;
        long min = 0;
        long sum = 0;
        long begin = System.currentTimeMillis();
        int count = 0;
        for (long i = 0; i < (NB_GAME_CHARACTER); i++) {
            long inter_begin = System.currentTimeMillis();
            List<Action> res = db.retrieve(new ConstraintCriteria<Long>("gameCharacterOrder", i));
            for (Action a : res) {
                GameDate d = a.getGameDate();
                GameDate n = new GameDate(d.getHours() + 1, d.getMoonDays() + 1, d.getYearMoons(), d.getYears());
                a.changeGameDate(n);
                db.save(a);
                count++;
            }
            db.commit();
            long inter_end = System.currentTimeMillis();
            max = Math.max(max, inter_end - inter_begin);
            min = min == 0 ? inter_end - inter_begin : Math.min(min, inter_end - inter_begin);
            sum += (inter_end - inter_begin);
        }
        long end = System.currentTimeMillis();
        log.info("\n" + NB_GAME_CHARACTER + " random List retrieve and modify in " + NB_INSERT + " elements\n" + "total=" + (end - begin) + "ms\n" + "sum=" + sum + "ms\n" + "max=" + max + "ms\n" + "min=" + min + "ms\n" + "moy=" + (((float) sum) / NB_GAME_CHARACTER) + "ms\n" + "moy nb action per commit=" + (((float) count) / NB_GAME_CHARACTER) + "\n");
        db.commit();
    }
}
