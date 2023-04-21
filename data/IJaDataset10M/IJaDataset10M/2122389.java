// $Id: BalanceSheet.java,v 1.1.1.1 2005/10/22 06:55:53 vcrfix Exp $
package bestbooks3.reports;

importreportsmport java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;

import bestbooks3.controller.*;


/** This class is used to display the balance sheet of the
  * Asset, Liability and Owner's Equity accounts.
  *
  * @author P. Ingle
  * @version 3.0
  * @since 1.0
  *
 */
public class BalanceSheet implements BestBooksReport {
    public static final String rcsid = "$Id: BalanceSheet.java,v 1.1.1.1 2005/10/22 06:55:53 vcrfix Exp $";
    
    private ChartOfAccounts coa = new ChartOfAccounts();

    private ArrayList   al;
    private ArrayList   types;

    private Ledger      account;

    private float       assetBalance = 0;
    private float       liabilityBalance = 0;

//    private JPanel      pane;
//    private JTextArea   area;
    
    private BestBooksClassLoader    bbcl = new BestBooksClassLoader();

    public BalanceSheet() {
//        super("Balance Sheet",false,true,false,true);
//        setSize(300,400);
//
//
//        pane = new JPanel();
//        area = new JTextArea(15,10);
//
//        al = coa.getNameList();
//        types = coa.getTypeList();
//
//        int i;
//        String typeValue;
//
//        area.append("Assets\n\n");
//        for (i=0;i<types.size();i++) {
//            typeValue = types.get(i).toString();
//            
//            if (typeValue.equals("Asset")) {
//                    account = (Ledger)bbcl.newInstance(coa.getClass(al.get(i).toString())); 
//                    account.read();
//                    assetBalance += account.getBalance();
//                    area.append(al.get(i).toString()+"-"+account.getBalance()+"\n");
//            }
//
//        }
//
//        area.append("\nTotal Assets "+assetBalance+"\n");
//
//        area.append("Liabilities & Owners Equity\n\n");
//        for (i=0;i<types.size();i++) {
//            typeValue = types.get(i).toString();

//           if (typeValue.equals("Liability") ||
//                typeValue.equals("OwnersEquity")) {
//                    account = (Ledger)bbcl.newInstance(coa.getClass(al.get(i).toString()));
//                    account.read();
//                    liabilityBalance += account.getBalance();
//                    area.append(al.get(i).toString()+"-"+account.getBalance()+"\n");
//            }
//        }
//
//        area.append("\nTotal liabilities & owners equity "+liabilityBalance+"\n");
//        pane.add(area);
//
//        setContentPane(pane);
    }

    /**
     * This method will return the formatted output of the balance sheet report as a String
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        
        int i;
        String typeValue;

        buffer.append("Assets\n\n");
        for (i=0;i<types.size();i++) {
            typeValue = types.get(i).toString();
            
            if (typeValue.equals("Asset")) {
                    account = (Ledger)bbcl.newInstance(coa.getClass(al.get(i).toString())); 
                    account.read();
                    assetBalance += account.getBalance();
                    buffer.append(al.get(i).toString()+"-"+account.getBalance()+"\n");
            }

        }

        buffer.append("\nTotal Assets "+assetBalance+"\n");

        buffer.append("Liabilities & Owners Equity\n\n");
        for (i=0;i<types.size();i++) {
            typeValue = types.get(i).toString();

           if (typeValue.equals("Liability") ||
                typeValue.equals("OwnersEquity")) {
                    account = (Ledger)bbcl.newInstance(coa.getClass(al.get(i).toString()));
                    account.read();
                    liabilityBalance += account.getBalance();
                    buffer.append(al.get(i).toString()+"-"+account.getBalance()+"\n");
            }
        }

        buffer.append("\nTotal liabilities & owners equity "+liabilityBalance+"\n");
        
        return buffer.toString();
    }

//    public void actionPerformed(ActionEvent evt) {
//    }

    public float getAssetBalance() {
        return assetBalance;
    }

    public float getLiabilityBalance() {
        return liabilityBalance;
    }
}
