package mailserver;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        MailServerImpl obj = new MailServerImpl();
        System.out.println(obj.listAllStorageGroup("test"));
    }
}
