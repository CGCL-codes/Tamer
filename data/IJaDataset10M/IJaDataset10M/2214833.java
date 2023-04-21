package newgen.utilities.cdsisisImport;

import java.util.ArrayList;

public class DirectoryList extends ArrayList {

    /**
  * default constructor*/
    public DirectoryList() {
    }

    /**
  * constructor with string as argument*/
    public DirectoryList(String s) throws MarcException {
        if ((s.length() % 12) != 0) throw new MarcException("Length of String is not multiple of 12");
        for (int i = 0; i < s.length(); ) {
            add(new Directory(s.substring(i, i + 12)));
            i += 12;
        }
    }

    /**
  * This method returns List of Directories
  * @return array of directories*/
    public Directory[] getAllDirectory() {
        Object[] b = toArray();
        Directory d[] = new Directory[b.length];
        for (int i = 0; i < b.length; i++) {
            d[i] = (Directory) b[i];
        }
        return d;
    }

    /**
	* It adds one Directory to the DirectoryList atthe end
	* @param It takes directory as argument which is added to the directory list at the end
	*/
    public void add(Directory d) {
        super.add(d);
    }

    /**
	* It returns Direcory at the desired index of DIrectoryList
	* @param It take integer as argument,here integer is a index in directory list 
	* @return It returns directory at the specified index*/
    public Directory getDirectoryAt(int i) {
        return (Directory) get(i);
    }
}
