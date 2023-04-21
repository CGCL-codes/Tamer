package ow.directory;

public class MultiValueDirectoryTest {

    public static void main(String[] args) throws Exception {
        String providerName = "BerkeleyDB";
        DirectoryProvider dirProvider = DirectoryFactory.getProvider(providerName);
        DirectoryConfiguration dirConfig = DirectoryConfiguration.getDefaultConfiguration();
        MultiValueDirectory<String, String> dir = dirProvider.openMultiValueDirectory(String.class, String.class, "./", "test", dirConfig);
        System.out.println(dir.get("abc"));
        System.out.println(dir.get("def"));
        dir.put("abc", "ABC");
        dir.put("def", "DEF");
        dir.put("abc", "GHI");
        dir.put("abc", "JKL");
        dir.put("abc", "JKL");
        dir.put("abc", "JKL");
        dir.put("abc", "JKL");
        dir.put("abc", "JKL");
        System.out.println(dir.get("abc") + " should be [ABC, GHI, JKL]");
        System.out.println(dir.get("def") + " should be [DEF]");
        System.out.println(dir.remove("abc", "AAA") + " should be null");
        System.out.println(dir.get("abc") + " should be [ABC, GHI, JKL]");
        System.out.println(dir.remove("abc", "GHI") + " should be GHI");
        System.out.println(dir.get("abc") + " should be [ABC, JKL]");
        System.out.println("keySet: " + dir.keySet() + " should be [abc, def]");
        System.out.println(dir.remove("abc") + " should be [ABC, JKL]");
        System.out.println(dir.get("abc") + " should be null");
        System.out.println(dir.remove("def", "DEF") + " should be DEF");
        System.out.println("keySet: " + dir.keySet() + " should be []");
        Thread.sleep(5000);
        dir.close();
    }
}
