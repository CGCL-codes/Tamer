package genomicMap.outputModel;

import genomicMap.data.OrderElement;
import genomicMap.outputModel.options.AnnealOOptionsModel;
import java.util.Collection;
import java.util.LinkedHashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author stewari1
 */
@Entity
public class AnnealOModel extends OutputModel implements java.io.Serializable {

    public static final int FIG_GMAP = 0;

    public static final int FIG_PMAP = 1;

    public static final int FIG_FULLMAP = 2;

    public static final int FIG_IPMAP = 3;

    public static final int FIG_IGMAP = 4;

    public static final int FIG_IFULLMAP = 5;

    private double convValue = 0.0;

    private int contigCount = 0;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<OrderElement> orderSet = new LinkedHashSet<OrderElement>();

    @OneToOne(cascade = CascadeType.ALL)
    private AnnealOOptionsModel optionsModel = new AnnealOOptionsModel();

    /** Creates a new instance of AnnealOModel */
    public AnnealOModel() {
    }

    @Override
    public int hashCode() {
        return super.hashCode() + new Double(getConvValue()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AnnealOModel) {
            AnnealOModel annealOModel = (AnnealOModel) obj;
            return super.equals(annealOModel) && getConvValue() == annealOModel.getConvValue();
        }
        return false;
    }

    public int[] getOrder() {
        OrderElement[] array = (OrderElement[]) getOrderSet().toArray(new OrderElement[0]);
        int[] order = new int[array.length];
        for (int index = 0; index < order.length; index++) {
            order[index] = array[index].getVal();
        }
        return order;
    }

    public void setOrder(int[] order) {
        for (int val : order) {
            getOrderSet().add(new OrderElement(val));
        }
    }

    public AnnealOOptionsModel getOptionsModel() {
        return optionsModel;
    }

    public void setOptionsModel(AnnealOOptionsModel optionsModel) {
        this.optionsModel = optionsModel;
    }

    public double getConvValue() {
        return convValue;
    }

    public void setConvValue(double lHood) {
        this.convValue = lHood;
    }

    public Collection<OrderElement> getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(Collection<OrderElement> orderSet) {
        this.orderSet = orderSet;
    }

    public int getContigCount() {
        return contigCount;
    }

    public void setContigCount(int contigCount) {
        this.contigCount = contigCount;
    }
}
