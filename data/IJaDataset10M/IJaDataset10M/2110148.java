package net.sf.ofx4j.domain.data.profile.info.common;

import net.sf.ofx4j.meta.Aggregate;
import net.sf.ofx4j.meta.Element;

/**
 * Image Profile
 * @author Scott Priddy
 * @see "Section 3.1.6.2 OFX Spec"
 */
@Aggregate("IMAGEPROF")
public class ImageProfile {

    private Boolean closingImageAvailable;

    private Boolean transactionImageAvailable;

    @Element(name = "CLOSINGIMGAVAIL", required = true, order = 10)
    public Boolean getClosingImageAvailable() {
        return closingImageAvailable;
    }

    public void setClosingImageAvailable(Boolean closingImageAvailable) {
        this.closingImageAvailable = closingImageAvailable;
    }

    @Element(name = "TRANIMGAVAIL", required = true, order = 20)
    public Boolean getTransactionImageAvailable() {
        return transactionImageAvailable;
    }

    public void setTransactionImageAvailable(Boolean transactionImageAvailable) {
        this.transactionImageAvailable = transactionImageAvailable;
    }
}
