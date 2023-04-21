package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttributeSubName;

public class ArchivingAttributeSubNamesComparator implements Comparator<ArchivingAttributeSubName> {

    private transient Collator referenceComparator;

    public ArchivingAttributeSubNamesComparator() {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    @Override
    public int compare(ArchivingAttributeSubName o1, ArchivingAttributeSubName o2) {
        boolean doCompare = false;
        if (o1 != null && o2 != null) {
            if (o1 instanceof ArchivingAttributeSubName && o2 instanceof ArchivingAttributeSubName) {
                doCompare = true;
            }
        }
        if (doCompare) {
            String name1 = o1.getName();
            String name2 = o2.getName();
            return referenceComparator.compare(name1, name2);
        } else {
            return 0;
        }
    }
}
