package spaceopera.universe.colony;

import spaceopera.universe.SOConstants;
import spaceopera.universe.Universe;

public class BuildProject implements Cloneable, SOConstants {

    private int id = 0;

    private String name = "";

    private String type = "";

    private String description = "";

    private String picture = "";

    private int productId = 0;

    private int componentId = 0;

    private double[] productionCost;

    private double[] productionCostPaid;

    private double[] supportCost;

    private double[] producedResource;

    private double percentage;

    private double percentComplete;

    private boolean unique = false;

    private boolean useOnlyOnce = false;

    private boolean allowBuild = false;

    private double educationLevel = 10.0;

    private Universe universe;

    private double scanningCount = 0.0;

    public void setScanner(double d) {
        scanningCount += d;
    }

    public boolean getAllowBuild() {
        return (allowBuild);
    }

    public String getType() {
        return (type);
    }

    public int getComponent() {
        return (componentId);
    }

    public String getDescription() {
        return (description);
    }

    public double getEducationLevel() {
        return (educationLevel);
    }

    public int getId() {
        return (id);
    }

    public double getMaintenance() {
        return (supportCost[R_WORKUNITS]);
    }

    public String getName() {
        return (name);
    }

    public double getEfficiency() {
        return (percentage);
    }

    public String getPicture() {
        return (picture);
    }

    public int getProduct() {
        return (productId);
    }

    public double getProductionCost(int resource) {
        return (productionCost[resource]);
    }

    public double getProductionCostPaid(int resource) {
        return (productionCostPaid[resource]);
    }

    public double getResourceProduction(int resource) {
        return (producedResource[resource]);
    }

    public double getScanningCount() {
        return (scanningCount);
    }

    public double getSupportCost(int resource) {
        return (supportCost[resource]);
    }

    public boolean isUnique() {
        return (unique);
    }

    public boolean useOnlyOnce() {
        return (useOnlyOnce);
    }

    public void setAllowBuild(boolean b) {
        allowBuild = b;
    }

    public void setType(String c) {
        type = c;
    }

    public void setComponent(int c) {
        componentId = c;
    }

    public void setDescription(String d) {
        description = d;
    }

    public void setEducationLevel(double e) {
        educationLevel = e;
    }

    public void setId(int i) {
        id = i;
    }

    public void setName(String n) {
        name = n;
    }

    public void setEfficiency(double p) {
        percentage = p;
    }

    public void setPercentComplete(double p) {
        percentComplete = p;
    }

    public void setPicture(String p) {
        picture = p;
    }

    public void setProduct(int p) {
        productId = p;
    }

    public void setProductionCostPaid(int res, double c) {
        productionCostPaid[res] = c;
    }

    public void setUnique(boolean u) {
        unique = u;
    }

    public void setUniverse(Universe u) {
        universe = u;
    }

    public void setUseOnlyOnce(boolean u) {
        useOnlyOnce = u;
    }

    public BuildProject() {
        productionCost = new double[R_MAX];
        producedResource = new double[R_MAX];
        supportCost = new double[R_MAX];
        productionCostPaid = new double[R_MAX];
        for (int i = R_MIN; i < R_MAX; i++) {
            productionCost[i] = 0.0;
            producedResource[i] = 0.0;
            supportCost[i] = 0.0;
            productionCostPaid[i] = 0.0;
        }
        percentage = 100;
    }

    public Object clone() {
        BuildProject object = null;
        try {
            object = (BuildProject) super.clone();
            object.productionCost = (double[]) productionCost.clone();
            object.producedResource = (double[]) producedResource.clone();
            object.supportCost = (double[]) supportCost.clone();
            object.productionCostPaid = (double[]) productionCostPaid.clone();
        } catch (Exception e) {
            System.out.println("Buildproject clone failed. " + e);
        }
        return object;
    }

    public double getRushJobCost() {
        double cost = 0.0;
        for (int i = R_MIN; i < R_MAX; i++) {
            if (productionCost[i] > 0) {
                cost += productionCost[i];
            }
        }
        return cost;
    }

    public double getPercentComplete() {
        double percent = 0.0;
        double minPercent = 100.0;
        double prod = 0.0;
        double paid = 0.0;
        for (int i = R_MIN; i < R_MAX; i++) {
            if (productionCost[i] > 0) {
                prod = productionCost[i];
                paid = productionCostPaid[i];
                percent = 100.0 / prod * paid;
                if (percent > 99.9999) {
                    percent = 100.0;
                }
                if (percent < minPercent) {
                    minPercent = percent;
                }
            }
        }
        return (minPercent);
    }

    public double getPercentComplete(int resource) {
        double percent = 0.0;
        double minPercent = 100.0;
        double prod = 0.0;
        double paid = 0.0;
        if (productionCost[resource] > 0) {
            prod = productionCost[resource];
            paid = productionCostPaid[resource];
            percent = 100.0 / prod * paid;
            if (percent > 99.9999) {
                percent = 100.0;
            }
        }
        return (percent);
    }

    public void setProductionCost(int resource, double amount) {
        productionCost[resource] = amount;
    }

    public void setResourceProduction(int resource, double amount) {
        producedResource[resource] = amount;
    }

    public void setSupportCost(int resource, double amount) {
        supportCost[resource] = amount;
    }

    public void setAllProductionCostPaid() {
        for (int i = R_MIN; i < R_MAX; i++) {
            productionCostPaid[i] = productionCost[i];
        }
    }
}
