package de.robowars.ui.resource;

/**
 * @author Apollo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
class StaticLayerImpl extends StaticLayer {

    private final StaticLayerResource resource;

    public StaticLayerImpl(StaticLayerResource r, String text) {
        setIconTextGap(5);
        if (r != null) {
            setIcon(r.getIcon());
        }
        if (text != null) {
            setText(text);
        }
        this.resource = r;
    }

    /**
	 * @see de.robowars.ui.resource.StaticLayer#getLayerResource()
	 */
    public StaticLayerResource getLayerResource() {
        return resource;
    }

    public int getIconWidth() throws ResourceException {
        if (resource != null) {
            return (int) resource.getIcon().getIconWidth();
        } else {
            throw new ResourceException("No Icon defined in this layer");
        }
    }

    public int getIconHeight() throws ResourceException {
        if (resource != null) {
            return (int) resource.getIcon().getIconWidth();
        } else {
            throw new ResourceException("No Icon defined in this layer");
        }
    }
}
