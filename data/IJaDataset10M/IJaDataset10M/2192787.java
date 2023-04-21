/*
 * Copyright(c) TIS All Rights Reserved.
 */
package org.exploreRuncher.action;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.exploreRuncher.AbstractPreferenceInitializer1;
import org.exploreRuncher.ExploreRuncherPlugin;

/**
 * ���[�ς��炷
 * 
 * @author G.Sukigara
 */
public abstract class AbstractExploreRuncherAction implements
        IWorkbenchWindowActionDelegate {

    /** �I�����Ă������ */
    private Object selected = null;

    /** ������ǂ� */
    private IWorkbenchWindow workbenchWindow = null;

    /**
     * �t�@�C�����J�����s����
     * 
     * @param action action
     */
    public void run(IAction action) {

        if (selected == null) {
            MessageDialog.openInformation(new Shell(), "�G�N�X�v���[���[�����`���[",
                    "�����I������ĂȂ��񂾂Ȃ�");
            return;
        }

        try {
            String exploreCommand = doAction();
            Runtime.getRuntime().exec(exploreCommand);
        } catch (MessageException me) {
            MessageDialog.openInformation(new Shell(), "�G�N�X�v���[���[�����`���[",
                    me.getMessage());
        } catch (Throwable th) {
            MessageDialog.openInformation(new Shell(), "�G�N�X�v���[���[�����`���[",
                    "���߂񂾂Ȃ��@�s����Ȃ�\n" + th);
        }
    }

    /**
     * ����A�N�V�����̃R�}���h��ԋp����B ���̂߂����ǂ��������邱�ƁB
     * @return String ���s�R�}���h
     * @throws Exception �G���[
     */
    protected abstract String doAction() throws MessageException;

    /**
     * �f�B���N�g�����擾����B
     * @return File �I�����Ă���t�@�C����File�N���X
     */
    protected File getFilePath() {
        File directory = null;
        if (selected instanceof IResource) {
            directory = new File(((IResource) selected).getLocation()
                    .toOSString());
        } else if (selected instanceof File) {
            directory = (File) selected;
        }
        return directory;
    }
    
    /**
     * �t�@�C���t���p�X�̕�����\�L���擾����B
     * @return �t�@�C���t���p�X�̕�����\�L
     */
    protected String getFilePathString() {
        return getFilePath().toString().replace("\\", "\\\\");
    }

    /**
     * Java�\�L�̃t���p�X���擾����B
     * 
     * @return Java�\�L�̃t���p�X
     * @throws Exception �G���[
     */
    protected String getJavaPathString() throws MessageException{

        String javaPath = "";

        if (selected instanceof IResource) {
            IPath path = ((IResource) selected).getProjectRelativePath();
            javaPath = path.removeFileExtension().toString();
            javaPath = javaPath.replaceAll("/", ".");

            try {
            IProject project = ((IResource) selected).getProject();
            IProjectNature na = project.getNature("org.eclipse.jdt.core.javanature");

            if (na instanceof JavaProject) {
                JavaProject javaProject = (JavaProject)na;
                    
                IPackageFragmentRoot[] pkgRoot = javaProject.getPackageFragmentRoots();
                for (int i = 0; i < pkgRoot.length; i++) {
                    IPackageFragmentRoot root = pkgRoot[i];
                    if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
                        // �\�[�X�z�u�f�B���N�g���̏ꍇ�B
                        String fragmentName = root.getElementName();
                        if (javaPath.startsWith(fragmentName)) {
                            javaPath = javaPath.replaceFirst(fragmentName + ".", "");
                        }
                    }
                }
            } else {
                throw new MessageException("java�t�@�C�����I������Ă��܂���B");
            }
            
            } catch (CoreException ce ) {
                throw new MessageException("Java�t�@�C���̃t���p�X�擾���ɃG���[���������܂����B", ce);
            }
        } else {
            throw new MessageException("���\�[�X���I������Ă��܂���B");
        }
        return javaPath;

    }

    /**
     * �I��ύX���Ɍ��݊J���Ă���t�@�C�����擾����B
     * 
     * �G�N�X�v���[���[��ʂ̏ꍇ�̓Z���N�V��������擾�B �G�f�B�^�̏ꍇ��Window����擾�B
     * 
     * @param action action
     * @param selection �I�����Ă������
     */
    public void selectionChanged(IAction action, ISelection selection) {

        IAdaptable adaptable = null;
        if (selection instanceof IStructuredSelection) {
            adaptable = (IAdaptable) ((IStructuredSelection) selection)
                    .getFirstElement();
            if (adaptable instanceof IResource) {
                this.selected = (IResource) adaptable;
            } else {
                this.selected = (IResource) adaptable
                        .getAdapter(IResource.class);
            }
        } else {
            adaptable = (IAdaptable) workbenchWindow.getActivePage()
                    .getActiveEditor().getEditorInput().getAdapter(
                            IResource.class);
            selected = (IResource) adaptable.getAdapter(IResource.class);
        }

    }

    /**
     * �����
     */
    public void dispose() {
        // NOP
    }

    /**
     * ���������͗�������Ă���
     * 
     * @param window ������ǂ�
     */
    public void init(IWorkbenchWindow window) {

        workbenchWindow = window;
        selected = null;
        IAdaptable adaptable = null;

        ISelection selection = workbenchWindow.getSelectionService()
                .getSelection();
        if (selection instanceof IStructuredSelection) {
            adaptable = (IAdaptable) ((IStructuredSelection) selection)
                    .getFirstElement();
            if (adaptable instanceof IResource) {
                this.selected = (IResource) adaptable;
            } else {
                this.selected = (IResource) adaptable
                        .getAdapter(IResource.class);
            }
        } else {
            adaptable = (IAdaptable) workbenchWindow.getActivePage()
                    .getActiveEditor().getEditorInput().getAdapter(
                            IResource.class);
            selected = (IResource) adaptable.getAdapter(IResource.class);
        }
    }
    
    /**
     * �������ɋL�q���ꂽJava�p�X�Ȃǂ�u������B
     * @param arg �Ђ�����
     * @return �u��������ꂽ������
     */
    protected String editArgments(String arg) throws MessageException{
        String des = arg;
        des = des.replaceAll("%JAVA_PATH%", getJavaPathString());
        des = des.replaceAll("%FILE_PATH%", getFilePathString());
        return des;
    }
    
    /**
     * Store�ɕۑ����ꂽ�ݒ���擾
     * @param id �ݒ�L�[
     * @return �ݒ���e
     */
    protected String getProperty(String id) {
        IPreferenceStore store = ExploreRuncherPlugin.getDefault().getPreferenceStore();
        return store.getString(id);
    }
    
}
