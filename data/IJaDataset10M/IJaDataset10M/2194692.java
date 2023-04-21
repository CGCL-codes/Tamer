package org.tubs.epoc.SMFF.SystemFactories.Mappers.Implementations.StdMapper;

import org.jdom.Element;
import org.tubs.epoc.SMFF.SystemFactories.Mappers.AbstractMapperData;

/**
 * A mapper data extension to be used with class SensActMapper.
 * 
 * @see AbstractMapperData 
 * @see SensActMapper
 *
 */
public class SensActMapperData extends AbstractMapperData {

    private float kPredecessor = 3.0f;

    private float kSuccessor = 3.0f;

    private float kSameApp = 0.0000000001f;

    private float kResDist = 1.5f;

    /**
   * Constructs a new instance of the Mapper data.
   * @param predecessor factor that is applied to mass if predecessor is on that resource
   * @param successor factor that is applied to mass if successor is on that resource
   * @param sameApp factor that is applied to mass if resource has part of the same application which is not predecessor or successor
   * @param resDist factor that is applied to mass per distance between two resources
   * @param seed seed to be used for random number generation
   */
    public SensActMapperData(float predecessor, float successor, float sameApp, float resDist, long seed) {
        super(seed);
        kPredecessor = predecessor;
        kSuccessor = successor;
        kSameApp = sameApp;
        kResDist = resDist;
    }

    /**
   * Writes the factory data to XML.
   * @return the data as XML element.
   */
    @Override
    public Element toXML() {
        Element root = new Element("MapperFactory");
        root.setAttribute("classname", this.getClass().getName());
        root.setAttribute("seed", String.valueOf(this.seed));
        root.setAttribute("kPredecessor", String.valueOf(this.kPredecessor));
        root.setAttribute("kSuccessor", String.valueOf(this.kSuccessor));
        root.setAttribute("kSameApp", String.valueOf(this.kSameApp));
        root.setAttribute("kResDist", String.valueOf(this.kResDist));
        return root;
    }

    /**
   * Constructs a new instance of the Mapper data.
   * @param seed seed to be used for random number generation
   */
    public SensActMapperData(long seed) {
        super(seed);
    }

    /**
   * Getter method for the <tt>predecessor</tt> factor.
   * <p>
   * A <tt>predecessor</tt> is the factor that is applied to mass if predecessor is on that resource.
   * @return the <tt>predecessor</tt>
   */
    public float getKPredecessor() {
        return kPredecessor;
    }

    /**
   * Getter method for the <tt>successor</tt> factor.
   * <p>
   * A <tt>successor</tt> is the factor that is applied to mass if successor is on that resource.
   * @return the <tt>kSuccessor</tt>
   */
    public float getKSuccessor() {
        return kSuccessor;
    }

    /**
   * Getter method for the <tt>sameApp</tt> factor.
   * <p>
   * A <tt>sameApp</tt> is the factor that is applied to mass if resource has part of the same application which 
   * is not predecessor or successor.
   * @return the <tt>sameApp</tt>
   */
    public float getKSameApp() {
        return kSameApp;
    }

    /**
   * Getter method for the <tt>resDist</tt> factor.
   * <p>
   * A <tt>resDist</tt> is the factor that is applied to mass per distance between two resources.
   * @return the <tt>resDist</tt>
   */
    public float getKResDist() {
        return kResDist;
    }
}
