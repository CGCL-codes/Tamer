package org.jbfilter.test.beans.factory;

import org.jbfilter.test.beans.Category;
import org.jbfilter.test.beans.Utils;
import org.jbfilter.test.beans.Work;

public class WorkFactory {

    private static WorkFactory instance;

    private ComposerFactory composerFactory = ComposerFactory.getInstance();

    private Work dieZauberfloete;

    private Work requiemMozart;

    private Work carmen;

    public static WorkFactory getInstance() {
        if (instance == null) {
            instance = new WorkFactory();
        }
        return instance;
    }

    private WorkFactory() {
        super();
        long id = 1;
        createDieZauberfloete(id++);
        createCarmen(id++);
        createRequiemMozart(id++);
    }

    public Work getDieZauberfloete() {
        return dieZauberfloete.clone();
    }

    public void createDieZauberfloete(long id) {
        dieZauberfloete = new Work();
        dieZauberfloete.setId(id);
        dieZauberfloete.setTitle("Die Zauberfl�te");
        dieZauberfloete.setCategory(Category.OPERA);
        dieZauberfloete.setComposer(composerFactory.getMozart());
        dieZauberfloete.setDateOfCreation(Utils.createDate("30/09/1791"));
    }

    public void createRequiemMozart(long id) {
        requiemMozart = new Work();
        requiemMozart.setId(id);
        requiemMozart.setTitle("Requiem");
        requiemMozart.setCategory(Category.ORATORIO);
        requiemMozart.setComposer(composerFactory.getMozart());
        requiemMozart.setDateOfCreation(Utils.createDate("02/01/1793"));
    }

    public Work getCarmen() {
        return carmen.clone();
    }

    public void createCarmen(long id) {
        carmen = new Work();
        carmen.setId(id);
        carmen.setTitle("Carmen");
        carmen.setCategory(Category.OPERA);
        carmen.setComposer(composerFactory.getBizet());
        carmen.setDateOfCreation(Utils.createDate("03/03/1875"));
    }

    public Work getRequiemMozart() {
        return requiemMozart;
    }
}
