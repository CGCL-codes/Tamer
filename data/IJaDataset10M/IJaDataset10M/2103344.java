package com.germinus.xpression.cms.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import com.germinus.util.Bag;
import com.germinus.util.HashBag;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.worlds.World;

public class ChronologicalEntry {

    private int year;

    private int month;

    public ChronologicalEntry(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public ChronologicalEntry(Calendar instance) {
        this.year = instance.get(Calendar.YEAR);
        this.month = instance.get(Calendar.MONTH);
    }

    public static Bag<ChronologicalEntry> entriesFromWorld(World world) {
        Bag<ChronologicalEntry> entriesBag = new HashBag<ChronologicalEntry>();
        Iterator<Content> recursiveContentIterator = world.getContentFolder().recursiveContentIterator();
        Calendar calendar = Calendar.getInstance();
        while (recursiveContentIterator.hasNext()) {
            Content content = recursiveContentIterator.next();
            Date creationDate = content.getCreationDate();
            calendar.setTime(creationDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            entriesBag.add(new ChronologicalEntry(year, month));
        }
        return entriesBag;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChronologicalEntry) {
            ChronologicalEntry other = (ChronologicalEntry) obj;
            return other.year == year && other.month == month;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new Integer(month).hashCode() + year;
    }

    @Override
    public String toString() {
        return (month + 1) + "/" + year;
    }
}
