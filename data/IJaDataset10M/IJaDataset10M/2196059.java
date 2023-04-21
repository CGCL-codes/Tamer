package net.sf.logshark.simple;

class MainIgnoreAllContentCommand implements MainCommand {

    private String m_fileAbolutePath;

    public void execute(MainData mainData) {
        MainFileState fileState = mainData.getFileState(m_fileAbolutePath);
        fileState.m_numberOfLinesToIgnore = fileState.m_lastNumberOfLines;
    }

    MainIgnoreAllContentCommand(String fileAbolutePath) {
        super();
        m_fileAbolutePath = fileAbolutePath;
    }
}
