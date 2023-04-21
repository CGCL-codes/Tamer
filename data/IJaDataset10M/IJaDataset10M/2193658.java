package org.gbif.checklistbank.service;

public interface NubLookupService {

    /**
   * Matches all usages of a given checklist with the nub and update the usage nub key
   * if the direct match was successful.
   * @param checklistId checklist to match and update usages.
   * @return percentage of successful nub matches from 0-100.
   */
    public double lookupChecklist(int checklistId);
}
