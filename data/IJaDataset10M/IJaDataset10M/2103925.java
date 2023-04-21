package radius.dict;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface VendorDictFactory {

    public void load();

    public void reload();

    public VendorDict getVendorDict(String vendorName);

    public VendorDict getVendorDict(int vendorId);

    public ValueDict getValueDict(String name, String enumName);
}
