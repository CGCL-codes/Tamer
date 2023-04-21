package com.k42b3.aletheia.filter;

import java.util.Properties;
import javax.swing.JPanel;

/**
 * ConfigFilter
 *
 * @author     Christoph Kappestein <k42b3.x@gmail.com>
 * @license    http://www.gnu.org/licenses/gpl.html GPLv3
 * @link       http://code.google.com/p/delta-quadrant
 * @version    $Revision: 3 $
 */
public abstract class ConfigFilterAbstract extends JPanel {

    public abstract String getName();

    public abstract void onLoad(Properties config);

    public abstract Properties onSave();

    public abstract boolean isActive();
}
