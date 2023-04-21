package unbbayes.gui.mebn.extension.ssbn;

import unbbayes.gui.mebn.extension.IPanelBuilder;
import unbbayes.prs.mebn.ssbn.ISSBNGenerator;

/**
 * This interface builds a panel to be an option panel for a given SSBN 
 * generator class {@link ISSBNGenerator}. 
 * This is a part of MEBN's SSBN algorithm's plugin infrastructure.
 * @author Shou Matsumoto
 *
 */
public interface ISSBNOptionPanelBuilder extends IPanelBuilder {

    /**
	 * Commits the changes done at the panel obtained from {@link #getPanel()}.
	 * In another words, it collects the attributes from {@link #getPanel()}
	 * and fills this {@link ISSBNGenerator} set by {@link #setSSBNGenerator(ISSBNGenerator)}
	 * @see #getPanel()
	 * @see #setSSBNGenerator(ISSBNGenerator)
	 */
    public void commitChanges();

    /**
	 * Discards the changes done at the panel obtained from {@link #getPanel()}.
	 * In another words, it resets the values of the attributes of {@link #getSSBNGenerator()}
	 * and/or {@link #getPanel()}
	 * @see #getPanel()
	 * @see #getSSBNGenerator()
	 */
    public void discardChanges();

    /**
	 * Sets the ssbn generator to be altered by the panel obtained from 
	 * {@link #getPanel()}
	 * @param ssbnGenerator
	 * @see #getPanel()
	 */
    public void setSSBNGenerator(ISSBNGenerator ssbnGenerator);

    /**
	 * The {@link ISSBNGenerator} obtained by this method must be 
	 * the ssbn algorithm edited by {@link #getPanel()},
	 * which is mostly the algorithm set by {@link #setSSBNGenerator(ISSBNGenerator)}
	 * (unless the implementation has chosen not to do so).
	 * 
	 * @return the currently managed {@link ISSBNGenerator}.
	 * @see #setSSBNGenerator(ISSBNGenerator)
	 * @see #getPanel()
	 */
    public ISSBNGenerator getSSBNGenerator();
}
