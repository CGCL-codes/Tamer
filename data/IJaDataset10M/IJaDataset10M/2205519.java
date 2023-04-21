package org.itsnat.comp.text;

/**
 * Is the interface of {@link ItsNatFormattedTextField} components
 * using HTML input text elements as user interface.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLInputTextFormatted(org.w3c.dom.html.HTMLInputElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLInputTextFormatted extends ItsNatHTMLInputText, ItsNatFormattedTextField {
}
