package mya_dc.compilation_server.threads;

import mya_dc.shared_classes.*;
import mya_dc.shared_classes.Message.EMessageType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.LinkedList;
import mya_dc.compilation_server.CompilationServer;
import mya_dc.compilation_server.gui.CompilationServerGUI;
import mya_dc.compilation_server.compilation.*;
import mya_dc.compilation_server.database.*;
import mya_dc.compilation_server.users.UserProjectManager;

/**
 * Thread to handle clients' requests.
 * 
 * @author      Marina Skarbovsky
 * <br>			MYA
 */
public class UserConnectionThread implements Runnable {

    /**
	 * Constructor
	 *
	 * @param UserID - User's identification details.
	 * @param bNewProject - Flag that signifies whether the project is new.
	 * @param Trans  - Transmitter for communication with the user.
	 * @param MainServerConnection - Compilation Server that runs the thread.
	 * @param GUI  - Compilation Server GUI renderer.
	 */
    public UserConnectionThread(MYA_Authentication UserID, boolean bNewProject, Transmitter Trans, CompilationServer MainServerConnection, CompilationServerGUI GUI) {
        m_Transmitter = Trans;
        m_bNewProject = bNewProject;
        m_MainServerConnection = MainServerConnection;
        m_UserID = UserID;
        m_sSourcesFolder = m_sCompilationFolder = Trans.getWorkingDirectory();
        m_CompilationServerGUI = GUI;
        m_UserProjectManager = MainServerConnection.getUserProjectManager();
    }

    /**
	 * Transfers user's project files to the user's directory on the server.
	 *
	 */
    void transferProjectFiles() throws Exception {
        Message NewMessage = null;
        Message SuccessMessage = new Message(EMessageType.SUCCESS, 44);
        do {
            NewMessage = m_Transmitter.receive();
            switch(NewMessage.getType()) {
                case FILE_TRANSFER:
                    {
                        m_Transmitter.send(SuccessMessage);
                        break;
                    }
                case OPERATION_CANCELED:
                    {
                    }
                case FILE_TRANSFER_DONE:
                    {
                        return;
                    }
                default:
                    {
                        Message ErrorMessage = new Message(EMessageType.UNEXPECTED_MESSAGE, "Expected a file transfer.\n");
                        m_Transmitter.send(ErrorMessage);
                    }
            }
        } while (true);
    }

    /**
	 * Retrieves available slaves from the master server
	 *
	 * @return An array of objects, containing ConnectionInfo to the slaves
	 */
    Object[] getSlaveList() throws InterruptedException {
        Object[] AddressArray = null;
        AddressArray = m_MainServerConnection.getSlaveList();
        if (AddressArray != null) {
            if (AddressArray.length > 0) {
                return AddressArray;
            }
        }
        return null;
    }

    /**
	 * Performs the compilation process and sends the result to the user.
	 *
	 * @param Builder - ProjectBuilder
	 */
    boolean compileProject(ProjectBuilder Builder) {
        Timestamp CurrTime = new Timestamp(new java.util.Date().getTime());
        DatabaseManager.updateProjectTimestamp(CurrTime, m_UserID.getProject(), m_UserID.getUserName());
        DatabaseManager.addCompilationEntry(m_UserID.getUserName(), m_UserID.getProject(), CurrTime);
        m_CompilationServerGUI.updateCompilationsHistoryTable();
        m_CompilationServerGUI.updateUserProjectInformationTable();
        Builder.doBuild();
        MYA_File Exectuable = new MYA_File(m_sCompilationFolder + "output\\" + Builder.m_compFlags.outputFileName);
        if (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errConnectionToSomeSlavesFailed) || (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errSendingFilesToSomeSlaves)) || (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errGettingFilesFromSomeSlaves))) {
            Message ErrorMessage = new Message(EMessageType.COMPILATION_UNABLE_TO_COMPILE, Builder.GetBuildErrors());
            try {
                m_Transmitter.send(ErrorMessage);
            } catch (Exception e) {
                return false;
            }
        } else if (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errPreprocessingFailed) || (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errCompilationFailed)) || (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errLinkerFailed)) || (Builder.wasError(ProjectBuilder.ProjectBuilderErrors.errWarnings))) {
            Message CompilationFailedMessage = new Message(EMessageType.COMPILATION_RESULT_ERRORS, Builder.GetBuildErrors());
            try {
                m_Transmitter.send(CompilationFailedMessage);
            } catch (Exception e) {
                return false;
            }
        } else {
            Message NoErrMessage = new Message(EMessageType.SUCCESS, 33);
            try {
                m_Transmitter.send(NoErrMessage);
            } catch (Exception e) {
                return false;
            }
        }
        if (Exectuable.exists() == true) {
            m_Transmitter.setWorkingDirectory(m_sCompilationFolder + "output\\");
            Message ExecutableMessage = new Message(EMessageType.FILE_TRANSFER, Exectuable);
            try {
                m_Transmitter.send(ExecutableMessage);
            } catch (Exception e) {
                return false;
            } finally {
                m_Transmitter.setWorkingDirectory(m_sSourcesFolder);
            }
        } else {
            Message CompilationErrMsg = new Message(EMessageType.COMPILATION_RESULT_ERRORS, null);
            try {
                m_Transmitter.send(CompilationErrMsg);
            } catch (Exception e) {
                return false;
            } finally {
                m_Transmitter.setWorkingDirectory(m_sSourcesFolder);
            }
        }
        Builder.markCompiled();
        return true;
    }

    /**
	 * Load project information from file.
	 *
	 */
    public void LoadProjectInformation() {
        m_Project = null;
        m_CurrentCompilationFlags = null;
        ObjectInputStream inStream = null;
        try {
            inStream = new ObjectInputStream(new FileInputStream(m_sProjectFileName));
            m_Project = (CompilationProject) inStream.readObject();
            m_CurrentCompilationFlags = (CompilationFlags) inStream.readObject();
            inStream.close();
        } catch (Exception e) {
            try {
                inStream.close();
            } catch (Exception e1) {
            }
        }
    }

    /**
	 * Saves current session's project information to file
	 * 
	 * @param Location - A path where the project file is located
	 * @param project - A project to save
	 * 
	 */
    public void SaveProjectInformation() {
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(m_sProjectFileName));
            outStream.writeObject(m_Project);
            outStream.writeObject(m_CurrentCompilationFlags);
            outStream.close();
            return;
        } catch (Exception e) {
            try {
                outStream.close();
            } catch (IOException e1) {
            }
        }
    }

    /**
	 * Runs the UserConnectionThread that serves the client's requests.
	 *
	 */
    @Override
    public void run() {
        try {
            boolean bResult = true;
            m_sSourcesFolder = m_sSourcesFolder + "sources\\" + m_UserID.getUserName() + "\\" + m_UserID.getProject();
            m_sCompilationFolder = m_sCompilationFolder + "compilation\\" + m_UserID.getUserName() + "\\" + m_UserID.getProject() + "\\";
            m_sProjectFileName = m_sCompilationFolder + "Project.bin";
            m_UserProjectManager.addConnectedUser(m_UserID.getUserName(), m_UserID.getProject());
            m_CompilationServerGUI.updateUserProjectInformationTable();
            m_Transmitter.setWorkingDirectory(m_sSourcesFolder);
            LinkedList<Object[]> DeadFilesList = null;
            if (!m_bNewProject) {
                LoadProjectInformation();
                if (m_Project == null) {
                    m_Project = new CompilationProject(m_UserID.getProject(), m_Transmitter.getWorkingDirectory());
                }
                if (m_CurrentCompilationFlags == null) {
                    m_CurrentCompilationFlags = new CompilationFlags();
                }
            } else {
                File tmpFile = new File(m_sCompilationFolder);
                tmpFile.mkdirs();
                m_CurrentCompilationFlags = new CompilationFlags();
                m_Project = new CompilationProject(m_UserID.getProject(), m_Transmitter.getWorkingDirectory());
            }
            while (bResult) {
                Message NewMessage = m_Transmitter.receive();
                switch(NewMessage.getType()) {
                    case COMPILATION_COMPILE_PROJECT:
                        {
                            Object[] SlaveList = getSlaveList();
                            if (SlaveList == null) {
                                Message ErrorMessage = new Message(EMessageType.COMPILATION_NO_SLAVES, null);
                                m_Transmitter.send(ErrorMessage);
                            } else {
                                ProjectBuilder Builder = new ProjectBuilder(m_Project, SlaveList, m_sCompilationFolder, m_Transmitter, m_CurrentCompilationFlags, m_bProjectChanged, DeadFilesList);
                                compileProject(Builder);
                            }
                            m_bProjectChanged = false;
                            break;
                        }
                    case FILE_TRANSFER_START:
                        {
                            transferProjectFiles();
                            m_bProjectChanged = true;
                            break;
                        }
                    case FILE_TRANSFER_DONE:
                        {
                            if (!m_bNewProject) {
                                m_bProjectChanged = false;
                            }
                            break;
                        }
                    case PROJECT_VERIFICATION_START:
                        {
                            DeadFilesList = MYA_FileTreeNode.removeDeadFiles((CompilationProject) NewMessage.getObject(), m_Project);
                            if (DeadFilesList.size() > 0) {
                                m_bProjectChanged = true;
                            }
                            m_Project.updateTree();
                            m_Transmitter.send(new Message(EMessageType.SUCCESS, null));
                            DatabaseManager.updateProjectSize(m_Project.size(), m_UserID.getProject(), m_UserID.getUserName());
                            m_CompilationServerGUI.updateUserProjectInformationTable();
                            break;
                        }
                    case COMPILATION_SET_FLAGS:
                        {
                            CompilationFlags NewFlags = (CompilationFlags) NewMessage.getObject();
                            if (!NewFlags.equals(m_CurrentCompilationFlags)) {
                                m_Project.markTreeAsNotSent();
                                m_bProjectChanged = true;
                            }
                            m_CurrentCompilationFlags = NewFlags;
                            break;
                        }
                    default:
                        {
                            Message ErrorMessage = new Message(EMessageType.UNEXPECTED_MESSAGE, "Expected compilation directive.\n");
                            m_Transmitter.send(ErrorMessage);
                            NewMessage = m_Transmitter.receive();
                        }
                }
            }
        } catch (Exception e1) {
            try {
                m_Transmitter.close();
            } catch (Exception e2) {
            }
            return;
        } finally {
            SaveProjectInformation();
            m_UserProjectManager.removeConnectedUser(m_UserID.getUserName(), m_UserID.getProject());
            m_CompilationServerGUI.updateUserProjectInformationTable();
        }
    }

    private Transmitter m_Transmitter = null;

    private CompilationServer m_MainServerConnection = null;

    private MYA_Authentication m_UserID = null;

    private CompilationProject m_Project = null;

    private String m_sSourcesFolder = null;

    private String m_sCompilationFolder = null;

    private String m_sProjectFileName = null;

    private CompilationFlags m_CurrentCompilationFlags = null;

    private CompilationServerGUI m_CompilationServerGUI;

    private UserProjectManager m_UserProjectManager = null;

    private boolean m_bNewProject = false;

    private boolean m_bProjectChanged = true;
}
