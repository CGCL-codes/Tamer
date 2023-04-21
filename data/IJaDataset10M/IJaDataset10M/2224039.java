package com.sts.webmeet.content.client.audio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataSource16khzTo8khz implements AtomicDataSource {

    private AtomicDataSource ads;

    private byte[] ba;

    public DataSource16khzTo8khz(AtomicDataSource ads) {
        this.ads = ads;
        this.ba = new byte[ads.getAtomSize() / 2];
    }

    public int getAtomSize() {
        return this.ba.length;
    }

    public byte[] getData() throws IOException {
        byte[] ba16K = this.ads.getData();
        for (int i = 0; i < this.ba.length; i += 2) {
            this.ba[i] = ba16K[i * 2];
            this.ba[i + 1] = ba16K[(i * 2) + 1];
        }
        return this.ba;
    }

    public void interruptConsumers() {
        ads.interruptConsumers();
    }

    public static void main(String[] args) throws Exception {
        File fileSource = new File(args[0]);
        final DataInputStream dis = new DataInputStream(new FileInputStream(fileSource));
        AtomicDataSource fileAds = new AtomicDataSource() {

            byte[] ba = new byte[20];

            public void interruptConsumers() {
            }

            public byte[] getData() throws IOException {
                dis.readFully(ba);
                return ba;
            }

            public int getAtomSize() {
                return ba.length;
            }
        };
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(args[0] + "_8khz"));
        DataSource16khzTo8khz downer = new DataSource16khzTo8khz(fileAds);
        for (int i = 0; i < fileSource.length(); i += 10) {
            dos.write(downer.getData());
        }
        dos.close();
    }
}
