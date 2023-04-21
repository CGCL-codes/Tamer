package com.jaccount.model;

import java.util.UUID;
import com.jaccount.buchhaltungsui.BuchhaltungsUI;
import com.jaccount.communication.AnwendungsInitObject;
import com.jaccount.communication.ModelInitObject;

public class Buchhaltung {

    public String fileName = null;

    public String description = null;

    public UUID buchhaltungsId = null;

    public BuchungsProcessor buchungsProcessor = null;

    public KontoFactory kontoFactory = null;

    public SummenFactory summenFactory = null;

    public BuchungsRepository buchungsRepository = null;

    public ModelInitObject init = null;

    public AnwendungsInitObject anwendungsInitObject = null;

    public BuchhaltungsUI buchhaltungsUI = null;

    public Buchhaltung(AnwendungsInitObject anwendungsInitObject) {
        this.anwendungsInitObject = anwendungsInitObject;
        init = new ModelInitObject();
        init.buchhaltung = this;
        buchhaltungsId = UUID.randomUUID();
        init.buchhaltungsId = buchhaltungsId;
        kontoFactory = new KontoFactory(init);
        kontoFactory.init();
        init.kontoFactory = kontoFactory;
        summenFactory = new SummenFactory(init);
        summenFactory.init();
        buchungsRepository = new BuchungsRepository(init);
        init.buchungsRepository = buchungsRepository;
        buchungsProcessor = new BuchungsProcessor(init);
    }
}
