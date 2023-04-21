package alessiolab.moneyreport;

import java.util.Set;
import java.util.TreeSet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Context {

    public String excelFilePath = null;

    public HSSFWorkbook excelFile = null;

    public Set<Transaction> txSet = new TreeSet<Transaction>();

    public AbstAccount[] accountList = new AbstAccount[] { new FinecoAccount(), new ContoArancioAccount(), new FoundsAccount(), new SuperSaveAccount() };

    public AbstAccount totalAccount = new TotalAccount();
}
