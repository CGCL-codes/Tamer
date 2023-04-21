package net.sf.yahoocsv.domain;

/**
 * Stock domain data 
 */
public class Stock extends Domain {

    private StockId stockId;

    private String name;

    private String ticker;

    private Integer sectorCode;

    private String sectorName;

    private Integer industryGroupCode;

    private String industryGroupName;

    private Integer industryCode;

    private String industryName;

    private Integer subIndustryCode;

    private String subIndustryName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockId getStockId() {
        return stockId;
    }

    public void setStockId(StockId stockId) {
        this.stockId = stockId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(Integer industryCode) {
        this.industryCode = industryCode;
    }

    public Integer getIndustryGroupCode() {
        return industryGroupCode;
    }

    public void setIndustryGroupCode(Integer industryGroupCode) {
        this.industryGroupCode = industryGroupCode;
    }

    public String getIndustryGroupName() {
        return industryGroupName;
    }

    public void setIndustryGroupName(String industryGroupName) {
        this.industryGroupName = industryGroupName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public Integer getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(Integer sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public Integer getSubIndustryCode() {
        return subIndustryCode;
    }

    public void setSubIndustryCode(Integer subIndustryCode) {
        this.subIndustryCode = subIndustryCode;
    }

    public String getSubIndustryName() {
        return subIndustryName;
    }

    public void setSubIndustryName(String subIndustryName) {
        this.subIndustryName = subIndustryName;
    }

    public int hashCode() {
        int code = 1 << 8;
        if (stockId != null) code ^= stockId.hashCode();
        if (name != null) code ^= name.hashCode();
        if (ticker != null) code ^= ticker.hashCode();
        if (sectorCode != null) code ^= sectorCode.hashCode();
        if (sectorName != null) code ^= sectorName.hashCode();
        if (industryGroupCode != null) code ^= industryGroupCode.hashCode();
        if (industryGroupName != null) code ^= industryGroupName.hashCode();
        if (industryCode != null) code ^= industryCode.hashCode();
        if (industryName != null) code ^= industryName.hashCode();
        if (subIndustryCode != null) code ^= subIndustryCode.hashCode();
        if (subIndustryName != null) code ^= subIndustryName.hashCode();
        return code;
    }

    public String toString() {
        return "(" + getTicker() + ")\t" + getName();
    }
}
