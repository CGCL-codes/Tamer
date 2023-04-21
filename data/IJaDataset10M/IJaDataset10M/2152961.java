package com.unicont.cardio.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.unicont.cardio.Cardiogram;
import com.unicont.cardio.Computable;
import com.unicont.cardio.Lead;
import com.unicont.cardio.impl.ComputableCardiogramImpl;
import com.unicont.cardio.impl.Units;

public class SaveUtils {

    public static void saveCardiogramToFile(Cardiogram aCardiogram, String aFileName) throws FileNotFoundException, IOException {
        String date = new SimpleDateFormat("yyMMddHm").format(new Date(aCardiogram.getTime()));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(aFileName));
        if (aCardiogram instanceof Computable) {
            Units units = ComputableCardiogramImpl.getUnits();
            ComputableCardiogramImpl.setUnits(Units.PAGER_UNITS);
            dumpToZipStream(new SaveUtils.ReceivedLeadsIterator(aCardiogram), date, zos);
            ComputableCardiogramImpl.setUnits(units);
        } else {
            dumpToZipStream(new SaveUtils.AllLeadsIterator(aCardiogram), date, zos);
        }
        zos.closeEntry();
        zos.close();
    }

    private static void dumpToZipStream(Iterator<Lead> aCardioIterator, String date, ZipOutputStream zos) throws IOException {
        int j = 0;
        while (aCardioIterator.hasNext()) {
            Lead l = aCardioIterator.next();
            ZipEntry ze = new ZipEntry(date + j + "_" + (j % 2 == 0 ? "1" : "2"));
            j++;
            zos.putNextEntry(ze);
            short[] data = l.getData();
            String content = "";
            for (short value : data) {
                content += value + "\n";
            }
            zos.write(content.getBytes());
        }
    }

    static class ReceivedLeadsIterator implements Iterator<Lead> {

        private Cardiogram cardiogram;

        int leadPointer = 0;

        public ReceivedLeadsIterator(Cardiogram aCardiogram) {
            cardiogram = aCardiogram;
        }

        public Lead next() {
            return cardiogram.getLead(Computable.RECEIVED_LEADS[leadPointer++]);
        }

        public boolean hasNext() {
            return leadPointer < Computable.RECEIVED_LEADS.length;
        }

        public void remove() {
        }
    }

    static class AllLeadsIterator implements Iterator<Lead> {

        private Cardiogram cardiogram;

        int leadPointer = 0;

        public AllLeadsIterator(Cardiogram aCardiogram) {
            cardiogram = aCardiogram;
        }

        public Lead next() {
            return cardiogram.getLead(leadPointer++);
        }

        public boolean hasNext() {
            return leadPointer < Cardiogram.LEADSIZE;
        }

        public void remove() {
        }
    }
}
