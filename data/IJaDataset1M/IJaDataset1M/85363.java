package pl.xperios.toolers.view.chooser;

import pl.xperios.toolers.common.Returnable;
import pl.xperios.toolers.domains.Table;

/**
 *
 * @author Praca
 */
public interface TableLangChooser {

    public void getTable(Returnable<Table> returnable) throws Exception;
}
