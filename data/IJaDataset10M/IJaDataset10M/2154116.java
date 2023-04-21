package net.sourceforge.piqle.qlearning;

import java.io.Serializable;

/**
 * When no values are yet stored for a (s,a) couple, the IRewardStore
 * (or its implemented classes) asks this class for a default value to return.
 * Values might be constant (@see ConstantValueChooser) or inside an interval 
 * (@see IntervalValueChooser).
 * Other strategies can be thought of. 
 * @author decomite
 *
 */
public interface IDefaultValueChooser extends Serializable {

    public double getValue();
}
