package gawky.processor;

public class ImportFIAS implements ImportI {

    public void execute(String infile, Import importer) throws Exception {
        BaseObjectI obj = new BaseObject();
        obj.setClientid("123");
        obj.setAmount("2");
        long totalamount = 0;
        for (int i = 0; i < 10000; i++) {
            if (!obj.getAmount().equals("")) totalamount += Long.parseLong(obj.getAmount());
            importer.run(obj);
        }
    }
}
