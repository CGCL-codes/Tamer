package gnu.kinsight.search;

import java.util.Collection;
import java.util.Set;
import gnu.kinsight.Date;
import gnu.kinsight.Family;
import gnu.kinsight.Kinsight;
import gnu.kinsight.Person;
import gnu.kinsight.photo.Photo;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Search.java
 *
 *
 * @author 
 * @version $Revision: 1.3 $
 */
public class PhotoSearchPanel extends SearchPanel {

    PhotoTableModel _photoTableModel = new PhotoTableModel();

    PersonSearcher _personSearcher = new PersonSearcher() {

        public Set getPeople(Object p) {
            return ((Photo) p).getPeople();
        }

        public String toString() {
            return "Photo";
        }

        public Family getFamily() {
            return _family;
        }
    };

    FieldSearcher[] _data = new FieldSearcher[] { _personSearcher, new TextSearcher() {

        public String getText(Object p) {
            return ((Photo) p).getCaption();
        }

        public String toString() {
            return "Caption";
        }
    }, new TextSearcher() {

        public String getText(Object p) {
            return ((Photo) p).getTitle();
        }

        public String toString() {
            return "Title";
        }
    }, new DateSearcher() {

        public Date getDate(Object p) {
            return ((Photo) p).getDate();
        }

        public String toString() {
            return "Date";
        }
    } };

    class PhotoTableModel extends SearchTableModel {

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int row, int column) {
            Photo p = (Photo) list.get(row);
            switch(column) {
                case 0:
                    return p.getTitle();
                case 1:
                    return p.getDate();
                case 2:
                    {
                        List people = new ArrayList(p.getPeople());
                        Collections.sort(people);
                        String s = people.toString();
                        return s.substring(1, s.length() - 1);
                    }
                default:
                    return "";
            }
        }

        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Title";
                case 1:
                    return "Date";
                case 2:
                    return "People";
                default:
                    return "";
            }
        }
    }

    public PhotoSearchPanel(Family f) {
        super(f);
        init();
    }

    public void searchForPerson(Person p) {
        _personSearcher.matchExact();
        _personSearcher.setPerson(p);
        search();
    }

    SearchTableModel getModel() {
        return _photoTableModel;
    }

    void onSelect(int modelIdx) {
        Kinsight.KINSIGHT.showPhoto((Photo) _photoTableModel.get(modelIdx));
    }

    Collection getAllItems() {
        return _family.getPhotos();
    }

    FieldSearcher[] getSearchData() {
        return _data;
    }
}
