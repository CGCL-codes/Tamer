package SampleApplications.PhotoSharing.XMLCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author gracep
 */
public class PhotoImpl {

    class PhotoData {

        public String PhotoID;

        public ArrayList<String> Keywords;

        public ArrayList<String> Comments;

        public byte[] File;

        public PhotoData(String id, byte[] File) {
            this.Keywords = new ArrayList<String>();
            this.Comments = new ArrayList<String>();
            this.PhotoID = id;
            this.File = File;
        }

        public int addComment(String comment) {
            int position = this.Comments.size();
            this.Comments.add(comment);
            return position;
        }

        public void deleteComment(int position) {
            this.Comments.remove(position);
        }

        public void addKeyword(String keyword) {
            this.Keywords.add(keyword);
        }
    }

    private HashMap<String, PhotoData> PhotoBase;

    public PhotoImpl() {
        this.PhotoBase = new HashMap<String, PhotoData>();
        PhotoData pd = new PhotoData("U34TYX", null);
        pd.Comments.add("Picture of a bicycle");
        pd.Keywords.add("bike");
        this.PhotoBase.put("U34TYX", pd);
        PhotoData pd2 = new PhotoData("U34TYJ", null);
        pd2.Comments.add("Another picture of a bicycle");
        pd2.Keywords.add("bike");
        this.PhotoBase.put("U34TYJ", pd2);
    }

    public String[] photoMetaData(String query) {
        ArrayList<String> results = new ArrayList<String>();
        Set<String> lstElems = PhotoBase.keySet();
        Iterator<String> itt = lstElems.iterator();
        while (itt.hasNext()) {
            PhotoData tmpMetaData = PhotoBase.get(itt.next());
            if (tmpMetaData.Keywords.contains(query)) {
                results.add(tmpMetaData.PhotoID);
            }
        }
        String[] returns = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            returns[i] = results.get(i);
        }
        System.out.println("PhotoMetaData Called: " + returns);
        return returns;
    }

    public String PHOTOFILE(String PhotoID) {
        PhotoData tmp = PhotoBase.get(PhotoID);
        return tmp.PhotoID;
    }

    public String[] PHOTOCOMMENT(String PhotoID) {
        PhotoData tmp = PhotoBase.get(PhotoID);
        String[] returns = new String[tmp.Comments.size()];
        for (int i = 0; i < tmp.Comments.size(); i++) {
            returns[i] = tmp.Comments.get(i);
        }
        return returns;
    }

    public int AddCOMMENT(String PhotoID, String comment) {
        System.out.println(PhotoID);
        System.out.println(comment);
        PhotoData tmp = PhotoBase.get(PhotoID);
        tmp.addComment(comment);
        return 0;
    }

    public int add(int x, int y) {
        return x + y;
    }

    public float addf(float x, float y) {
        return x + y;
    }

    public double addd(double x, double y) {
        System.out.println("add called");
        return x + y;
    }

    public boolean OR(boolean x, boolean y) {
        boolean C;
        C = x || y;
        System.out.println(x + " || " + y + " is " + C);
        return C;
    }

    public long addl(long x, long y) {
        return x + y;
    }

    public long sum(Object[] list) {
        long sum = 0;
        System.out.println("Sum called");
        for (int i = 0; i < list.length; i++) {
            sum += ((Integer) list[i]).intValue();
        }
        return sum;
    }

    public String append(Object[] list) {
        String sum = "";
        System.out.println("append called");
        for (int i = 0; i < list.length; i++) {
            sum += ((String) list[i]);
        }
        return sum;
    }

    public long suml(Object[] list) {
        long sum = 0;
        System.out.println("Suml called");
        for (int i = 0; i < list.length; i++) {
            sum += ((Long) list[i]).longValue();
        }
        return sum;
    }

    public double sumd(Object[] list) {
        double sum = 0;
        System.out.println("Sumd called");
        for (int i = 0; i < list.length; i++) {
            sum += ((Double) list[i]).doubleValue();
        }
        return sum;
    }

    public float sumf(Object[] list) {
        float sum = 0;
        System.out.println("Sumf called");
        for (int i = 0; i < list.length; i++) {
            sum += ((Float) list[i]).doubleValue();
        }
        return sum;
    }

    public boolean andList(Object[] list) {
        boolean sum = true;
        System.out.println("andList called");
        for (int i = 0; i < list.length; i++) {
            sum = sum && (((Boolean) list[i]).booleanValue());
            System.out.println("and: " + list[i]);
        }
        return sum;
    }

    public Object[] getInts() {
        Object[] ll = new Object[2];
        ll[0] = new Integer(4);
        ll[1] = new Integer(3);
        return ll;
    }

    public Object[] getDoubles() {
        Object[] ll = new Object[2];
        ll[0] = new Double(434.5);
        ll[1] = new Double(33.99);
        return ll;
    }

    public Object[] getFloats() {
        Object[] ll = new Object[2];
        ll[0] = new Float(4.5f);
        ll[1] = new Float(3.01f);
        return ll;
    }

    public Object[] getLongs() {
        Object[] ll = new Object[2];
        ll[0] = new Long(4);
        ll[1] = new Long(3);
        return ll;
    }

    public Object[] getBooleans() {
        Object[] ll = new Object[2];
        ll[0] = new Boolean(true);
        ll[1] = new Boolean(false);
        return ll;
    }

    public void VoidMethod() {
        System.out.println("End session");
    }
}
