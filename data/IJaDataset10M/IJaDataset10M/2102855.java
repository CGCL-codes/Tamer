package org.emftext.language.pl0.resource.pl0.ui;

public class Pl0CompletionProcessor implements org.eclipse.jface.text.contentassist.IContentAssistProcessor {

    private org.emftext.language.pl0.resource.pl0.ui.Pl0Editor editor;

    public Pl0CompletionProcessor(org.emftext.language.pl0.resource.pl0.ui.Pl0Editor editor) {
        this.editor = editor;
    }

    public org.eclipse.jface.text.contentassist.ICompletionProposal[] computeCompletionProposals(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        org.eclipse.emf.ecore.resource.Resource resource = editor.getResource();
        org.emftext.language.pl0.resource.pl0.IPl0TextResource textResource = (org.emftext.language.pl0.resource.pl0.IPl0TextResource) resource;
        String content = viewer.getDocument().get();
        org.emftext.language.pl0.resource.pl0.ui.Pl0CodeCompletionHelper helper = new org.emftext.language.pl0.resource.pl0.ui.Pl0CodeCompletionHelper();
        org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal[] computedProposals = helper.computeCompletionProposals(textResource, content, offset);
        org.emftext.language.pl0.resource.pl0.ui.Pl0ProposalPostProcessor proposalPostProcessor = new org.emftext.language.pl0.resource.pl0.ui.Pl0ProposalPostProcessor();
        java.util.List<org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal> computedProposalList = java.util.Arrays.asList(computedProposals);
        java.util.List<org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal> extendedProposalList = proposalPostProcessor.process(computedProposalList);
        if (extendedProposalList == null) {
            extendedProposalList = java.util.Collections.emptyList();
        }
        java.util.List<org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal> finalProposalList = new java.util.ArrayList<org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal>();
        for (org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal proposal : extendedProposalList) {
            if (proposal.getMatchesPrefix()) {
                finalProposalList.add(proposal);
            }
        }
        org.eclipse.jface.text.contentassist.ICompletionProposal[] result = new org.eclipse.jface.text.contentassist.ICompletionProposal[finalProposalList.size()];
        int i = 0;
        for (org.emftext.language.pl0.resource.pl0.ui.Pl0CompletionProposal proposal : finalProposalList) {
            String proposalString = proposal.getInsertString();
            String displayString = proposal.getDisplayString();
            String prefix = proposal.getPrefix();
            org.eclipse.swt.graphics.Image image = proposal.getImage();
            org.eclipse.jface.text.contentassist.IContextInformation info;
            info = new org.eclipse.jface.text.contentassist.ContextInformation(image, proposalString, proposalString);
            int begin = offset - prefix.length();
            int replacementLength = prefix.length();
            org.emftext.language.pl0.resource.pl0.ui.IPl0BracketHandler bracketHandler = editor.getBracketHandler();
            String closingBracket = bracketHandler.getClosingBracket();
            if (bracketHandler.addedClosingBracket() && proposalString.endsWith(closingBracket)) {
                replacementLength += closingBracket.length();
            }
            result[i++] = new org.eclipse.jface.text.contentassist.CompletionProposal(proposalString, begin, replacementLength, proposalString.length(), image, displayString, info, proposalString);
        }
        return result;
    }

    public org.eclipse.jface.text.contentassist.IContextInformation[] computeContextInformation(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        return null;
    }

    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    public org.eclipse.jface.text.contentassist.IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    public String getErrorMessage() {
        return null;
    }
}
