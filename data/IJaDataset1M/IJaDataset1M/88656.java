package org.hepotaiya.sample.simple.batch;

import org.zzz.hepotaiya.annotation.Entry;

public class Hoge2Batch {

    @Entry(phase = 2)
    public void hoge() {
        System.out.println("hoge2Batch#hoge");
    }

    @Entry(phase = 3)
    public String huga() {
        System.out.println("hoge2Batch#huga");
        return "success";
    }
}
