package org.pyre.finance.service;

import java.io.IOException;
import org.pyre.finance.view.VBalance;

public interface FundHistoryService {

    public abstract void searchAll();

    public abstract VBalance calculateBalance();

    public abstract void pasteHistory(String text) throws IOException;

    public abstract void savePaste(int[] rows);

    public abstract void search(int monthBefore, String keyword);
}
