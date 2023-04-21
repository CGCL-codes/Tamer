package twoadw.website.productmanufacturer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entity;
import org.modelibra.IDomainModel;
import org.modelibra.Oid;
import twoadw.website.Website;
import twoadw.website.product.Product;
import twoadw.website.manufacturer.Manufacturer;
import twoadw.website.manufacturer.Manufacturers;

/**
 * ProductManufacturer generated entity. This class should not be changed manually. 
 * Use a subclass to add specific code.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public abstract class GenProductManufacturer extends Entity<ProductManufacturer> {

    private static final long serialVersionUID = 1234729930222L;

    private static Log log = LogFactory.getLog(GenProductManufacturer.class);

    private String partNumber;

    private Long manufacturerOid;

    private Product product;

    private Manufacturer manufacturer;

    /**
	 * Constructs productManufacturer within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenProductManufacturer(IDomainModel model) {
        super(model);
    }

    /**
	     * Constructs productManufacturer within its parent(s).
	     * 
	        		* @param Product product
		    		* @param Manufacturer manufacturer
			     */
    public GenProductManufacturer(Product product, Manufacturer manufacturer) {
        this(manufacturer.getModel());
        setProduct(product);
        setManufacturer(manufacturer);
    }

    /**
		 * Sets partNumber.
		 * 
		 * @param partNumber
		 *            partNumber
		 */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
		 * Gets partNumber.
		 * 
		 * @return partNumber
		 */
    public String getPartNumber() {
        return partNumber;
    }

    /**
		 * Sets manufacturerOid.
		 * 
		 * @param manufacturerOid
		 *            manufacturerOid
		 */
    public void setManufacturerOid(Long manufacturerOid) {
        this.manufacturerOid = manufacturerOid;
        manufacturer = null;
    }

    /**
		 * Gets manufacturerOid.
		 * 
		 * @return manufacturerOid
		 */
    public Long getManufacturerOid() {
        return manufacturerOid;
    }

    /**
		 * Sets product.
		 * 
		 * @param product
		 *            product
		 */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
		 * Gets product.
		 * 
		 * @return product
		 */
    public Product getProduct() {
        return product;
    }

    /**
		 * Sets manufacturer.
		 * 
		 * @param manufacturer
		 *            manufacturer
		 */
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        if (manufacturer != null) {
            manufacturerOid = manufacturer.getOid().getUniqueNumber();
        } else {
            manufacturerOid = null;
        }
    }

    /**
		 * Gets manufacturer.
		 * 
		 * @return manufacturer
		 */
    public Manufacturer getManufacturer() {
        if (manufacturer == null) {
            Website website = (Website) getModel();
            Manufacturers manufacturers = website.getManufacturers();
            if (manufacturerOid != null) {
                manufacturer = manufacturers.getManufacturer(manufacturerOid);
            }
        }
        return manufacturer;
    }
}
