package openfuture.masterdata.test;

import java.util.Locale;
import java.util.Vector;
import openfuture.masterdata.IMasterDataContainer;
import openfuture.masterdata.IMasterDataSource;
import openfuture.masterdata.LDMasterDataContainer;

/**
 * The class LanguageDataSource is a simple (example) data source for
 * the master data class <tt>Language</tt>
 *
 * Creation date: (09.09.00 22:02:54)
 * @author: Markus Giebeler
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.<p>
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.<p>
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br>
 * http://www.gnu.org/copyleft/lesser.html"
 */
public class LanguageDataSource implements IMasterDataSource {

    /**
 * Constructor
 */
    public LanguageDataSource() {
        super();
    }

    /**
 * Provide some instances of <tt>Language</tt> as simple example
 */
    public IMasterDataContainer getMasterData(String type) {
        Vector v = new Vector();
        v.addElement(new Language("de", "de", "Deutsch", Locale.GERMANY));
        v.addElement(new Language("en", "de", "Englisch", Locale.UK));
        v.addElement(new Language("fr", "de", "Franz�sisch", Locale.FRANCE));
        v.addElement(new Language("it", "de", "Italienisch", Locale.ITALY));
        v.addElement(new Language("de", "en", "German", Locale.GERMANY));
        v.addElement(new Language("en", "en", "English", Locale.UK));
        v.addElement(new Language("fr", "en", "French", Locale.FRANCE));
        v.addElement(new Language("it", "en", "Italian", Locale.ITALY));
        v.addElement(new Language("de", "fr", "Allemande", Locale.GERMANY));
        v.addElement(new Language("en", "fr", "Anglaise", Locale.UK));
        v.addElement(new Language("fr", "fr", "Francais", Locale.FRANCE));
        v.addElement(new Language("it", "fr", "Italien", Locale.ITALY));
        return new LDMasterDataContainer(v);
    }
}
