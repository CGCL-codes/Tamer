package p400.srm404;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 05.06.2008
 * Time: 15:00:51
 */
public class ReadingBooks {

    public int countBooks(String[] readParts) {
        int count = 0;
        for (int i = 2; i < readParts.length; i++) {
            if (getIndex(readParts[i - 2]) + getIndex(readParts[i - 1]) + getIndex(readParts[i]) == 111) {
                i += 2;
                count++;
            }
        }
        System.out.println("count = " + count);
        return count;
    }

    public int getIndex(String name) {
        if ("story".equals(name)) {
            return 1;
        } else if ("introduction".equals(name)) {
            return 10;
        } else if ("edification".equals(name)) {
            return 100;
        }
        throw new RuntimeException("imposible value '" + name + "'");
    }

    public static void main(String[] args) {
        System.out.println(1 == new ReadingBooks().countBooks(new String[] { "introduction", "story", "introduction", "edification" }));
        System.out.println(2 == new ReadingBooks().countBooks(new String[] { "introduction", "story", "edification", "introduction", "story", "edification" }));
        System.out.println(5 == new ReadingBooks().countBooks(new String[] { "introduction", "story", "introduction", "edification", "story", "story", "edification", "edification", "edification", "introduction", "introduction", "edification", "story", "introduction", "story", "edification", "edification", "story", "introduction", "edification", "story", "story", "edification", "introduction", "story" }));
    }
}
