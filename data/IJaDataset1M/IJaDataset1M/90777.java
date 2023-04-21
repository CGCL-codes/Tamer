package com.aptana.ide.editors.unified.contentassist;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import com.aptana.ide.lexer.LexemeList;

/**
 * ICompletionProposalContributor
 */
public interface ICompletionProposalContributor {

    /**
	 * Generates the completion proposals for code assist
	 * @param viewer 
	 * @param offset The integer offset of the current insertion point
	 * @param position 
	 * @param lexemeList 
	 * @param activationChar 
	 * @param previousChar 
	 * @return An array of completion proposals, or null if not valid
	 */
    ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset, int position, LexemeList lexemeList, char activationChar, char previousChar);
}
