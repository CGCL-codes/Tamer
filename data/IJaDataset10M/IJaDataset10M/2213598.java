package net.sf.isolation.conversion;

import net.sf.isolation.core.IsoInformation;

/**
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>See implementation</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 296 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/conversion/IsoConverter.java $", lastChangedDate = "$LastChangedDate: 2011-01-07 02:37:02 -0500 (Fri, 07 Jan 2011) $")
public interface IsoConverter {

    <T> T convert(Class<T> type, Object value) throws IsoConversionException;
}
