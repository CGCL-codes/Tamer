package org.web3d.x3d.palette.items;

import org.web3d.x3d.types.SceneGraphStructureNodeType;

/**
 * INLINE_GridXYFixed.java
 * Created on 24 June 2009
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Don Brutzman, Mike Bailey
 * @version $Id$
 */
public class INLINE_GridXYFixed extends SceneGraphStructureNodeType {

    public INLINE_GridXYFixed() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public String createBody() {
        return "\n" + "    <Inline DEF='GridXY_20x20Fixed' url='\"GridXY_20x20Fixed.x3d\"\n" + "       \"../../../Savage/Tools/Authoring/GridXY_20x20Fixed.x3d\"\n" + "       \"https://savage.nps.edu/Savage/Tools/Authoring/GridXY_20x20Fixed.x3d\"\n" + "       \"GridXY_20x20Fixed.wrl\"\n" + "       \"../../../Savage/Tools/Authoring/GridXY_20x20Fixed.wrl\"\n" + "       \"https://savage.nps.edu/Savage/Tools/Authoring/GridXY_20x20Fixed.wrl\"'/>\n";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return null;
    }

    @Override
    public String getElementName() {
        return "GridXYFixed";
    }
}
