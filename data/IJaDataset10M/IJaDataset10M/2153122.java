package com.tonbeller.jpivot.olap.navi;

import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import com.tonbeller.jpivot.olap.model.MemberPropertyMeta;

/**
 * retrieves the description of the available member properties
 * @author av
 */
public interface MemberProperties {

    MemberPropertyMeta[] getMemberPropertyMetas(Level level);

    /**
   * Properties are either Level scope or Dimension scope.
   * Properties are unique within their scope. If scope is level, then
   * properties with same name in different levels are treated 
   * as different Properties.
   * If false, the scope will be Dimension.
   * @return
   */
    boolean isLevelScope();

    /**
   * returns a string <code>scope</code> 
   * that represents the scope of Member m.
   * The returned String <code>scope</code> 
   * ensures that <code>scope.equals(MemberPropertyMeta.getScope())</code> is 
   * true if the property belongs to member m. 
   */
    String getPropertyScope(Member m);

    /**
   * sets the visible properties. Optimizing implementations of
   * PropertyHolder may only return these properties.
   * @see com.tonbeller.jpivot.olap.model.PropertyHolder
   */
    void setVisibleProperties(MemberPropertyMeta[] props);
}
