package org.jabusuite.webclient.address.letter;

import java.util.List;
import java.util.Set;
import org.jabusuite.address.letter.Letter;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.webclient.main.JbsL10N;
import org.jabusuite.webclient.tables.TblJbsBaseObject;
import org.jabusuite.webclient.tables.TblMJbsBaseObject;

/**
 * This table holds the letters of an <code>Address</code>
 * @author hilwers
 * @date 2008-05-24
 */
public class TblAddressLetters extends TblJbsBaseObject {

    private static final long serialVersionUID = -3232754634174535437L;

    public TblAddressLetters(List jbsObjects) {
        super(jbsObjects);
    }

    @Override
    protected TblMJbsBaseObject createTableModel(Set jbsObjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected TblMJbsBaseObject createTableModel(List jbsObjects) {
        return new TblMAddressLetters(jbsObjects);
    }

    public class TblMAddressLetters extends TblMJbsBaseObject {

        private static final long serialVersionUID = 6654304711457668810L;

        public TblMAddressLetters(Set<Letter> addressLetters) {
            super(addressLetters);
        }

        public TblMAddressLetters(List<Letter> addressLetters) {
            super(addressLetters);
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return JbsL10N.getString("AddressLetter.letterDate");
                case 1:
                    return JbsL10N.getString("AddressLetter.name");
                default:
                    return "";
            }
        }

        public Object getValueAt(int column, int row) {
            if (row < this.getJbsBaseObjects().size()) {
                Letter addressLetter = (Letter) this.getJbsBaseObjects().get(row);
                switch(column) {
                    case 0:
                        String sDate = "";
                        if (addressLetter.getLetterDate() != null) sDate = ClientGlobals.getDateFormat().format(addressLetter.getLetterDate().getTime());
                        return sDate;
                    case 1:
                        String sName = addressLetter.getLetterSubject();
                        if (sName == null) sName = "";
                        return sName;
                    default:
                        return "";
                }
            } else return null;
        }
    }
}
