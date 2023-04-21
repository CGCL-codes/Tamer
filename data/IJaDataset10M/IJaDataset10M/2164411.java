package org.hermeneutix.model;

import java.io.Serializable;

public final class Word implements Serializable, Cloneable {

    /**
	 * event managing {@link PericopeModel}
	 */
    private PericopeModel model;

    /**
	 * contained origin text
	 */
    private String text;

    /**
	 * selected definition
	 */
    private String definition = null;

    /**
	 * comment text
	 */
    private String comment = null;

    public Word(final String word, final PericopeModel model) {
        this.model = model;
        this.text = word;
    }

    /**
	 * @return model
	 */
    public PericopeModel getModel() {
        return this.model;
    }

    /**
	 * @param model
	 *            model to set
	 */
    public void setModel(final PericopeModel model) {
        this.model = model;
    }

    /**
	 * @return text
	 */
    public String getText() {
        return this.text;
    }

    /**
	 * @param text
	 *            text to set
	 */
    public void setText(final String text) {
        this.text = text;
    }

    /**
	 * @return definition
	 */
    public String getDefinition() {
        return this.definition;
    }

    /**
	 * @param definition
	 *            definition to set
	 */
    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    /**
	 * @return comment
	 */
    public String getComment() {
        return this.comment;
    }

    /**
	 * @param comment
	 *            comment to set
	 */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
	 * creates a copy of this {@link Word}<br>
	 * WITHOUT setting its {@link PericopeModel}, which is recommended to be
	 * called separatly
	 * 
	 * @return cloned {@link Word} without model
	 */
    @Override
    public Word clone() {
        final Word cloned = new Word(this.text, null);
        cloned.comment = this.comment;
        cloned.definition = this.definition;
        return cloned;
    }
}
