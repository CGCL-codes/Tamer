package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFontAttribute;

/**
 * Font Style Command
 *
 * @author Phillip Beauvoir
 */
public class FontStyleCommand extends EObjectFeatureCommand {

    public FontStyleCommand(IFontAttribute object, String fontData) {
        super(Messages.FontStyleCommand_0, object, IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT, fontData);
    }
}
