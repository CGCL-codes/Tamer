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
 * すーぱくらす
 * 
 * @author G.Sukigara
 */
public abstract class AbstractExploreRuncherAction implements
        IWorkbenchWindowActionDelegate {

    /** 選択しているもの */
    private Object selected = null;

    /** ういんどう */
    private IWorkbenchWindow workbenchWindow = null;

    /**
     * ファイルを開く実行部分
     * 
     * @param action action
     */
    public void run(IAction action) {

        if (selected == null) {
            MessageDialog.openInformation(new Shell(), "エクスプローラーランチャー",
                    "何も選択されてないんだなも");
            return;
        }

        try {
            String exploreCommand = doAction();
            Runtime.getRuntime().exec(exploreCommand);
        } catch (MessageException me) {
            MessageDialog.openInformation(new Shell(), "エクスプローラーランチャー",
                    me.getMessage());
        } catch (Throwable th) {
            MessageDialog.openInformation(new Shell(), "エクスプローラーランチャー",
                    "ごめんだなも　不具合だなも\n" + th);
        }
    }

    /**
     * 動作アクションのコマンドを返却する。 このめそっどを実装すること。
     * @return String 実行コマンド
     * @throws Exception エラー
     */
    protected abstract String doAction() throws MessageException;

    /**
     * ディレクトリを取得する。
     * @return File 選択しているファイルのFileクラス
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
     * ファイルフルパスの文字列表記を取得する。
     * @return ファイルフルパスの文字列表記
     */
    protected String getFilePathString() {
        return getFilePath().toString().replace("\\", "\\\\");
    }

    /**
     * Java表記のフルパスを取得する。
     * 
     * @return Java表記のフルパス
     * @throws Exception エラー
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
                        // ソース配置ディレクトリの場合。
                        String fragmentName = root.getElementName();
                        if (javaPath.startsWith(fragmentName)) {
                            javaPath = javaPath.replaceFirst(fragmentName + ".", "");
                        }
                    }
                }
            } else {
                throw new MessageException("javaファイルが選択されていません。");
            }
            
            } catch (CoreException ce ) {
                throw new MessageException("Javaファイルのフルパス取得中にエラーが発生しました。", ce);
            }
        } else {
            throw new MessageException("リソースが選択されていません。");
        }
        return javaPath;

    }

    /**
     * 選択変更時に現在開いているファイルを取得する。
     * 
     * エクスプローラー画面の場合はセレクションから取得。 エディタの場合はWindowから取得。
     * 
     * @param action action
     * @param selection 選択しているもの
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
     * 空実装
     */
    public void dispose() {
        // NOP
    }

    /**
     * 初期化時は両方やっておく
     * 
     * @param window ういんどう
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
     * 引数内に記述されたJavaパスなどを置換する。
     * @param arg ひきすう
     * @return 置き換えられた文字列
     */
    protected String editArgments(String arg) throws MessageException{
        String des = arg;
        des = des.replaceAll("%JAVA_PATH%", getJavaPathString());
        des = des.replaceAll("%FILE_PATH%", getFilePathString());
        return des;
    }
    
    /**
     * Storeに保存された設定を取得
     * @param id 設定キー
     * @return 設定内容
     */
    protected String getProperty(String id) {
        IPreferenceStore store = ExploreRuncherPlugin.getDefault().getPreferenceStore();
        return store.getString(id);
    }
    
}
