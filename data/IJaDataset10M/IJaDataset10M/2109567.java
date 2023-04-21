/** 
 * OGL Explorer
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.leo.oglexplorer.model.engine.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.leo.oglexplorer.model.engine.SearchEngine;
import org.leo.oglexplorer.model.index.impl.FileSearchIndex;
import org.leo.oglexplorer.model.result.SearchResult;
import org.leo.oglexplorer.model.result.SearchType;
import org.leo.oglexplorer.model.result.impl.FileSearchResult;
import org.leo.oglexplorer.resources.Resources;
import org.leo.oglexplorer.ui.task.CancelMonitor;
import org.leo.oglexplorer.util.CustomRunnable;

/**
 * FileSystemSearchEngine $Id: FileSystemSearchEngine.java 4 2011-06-03
 * 11:13:39Z leolewis $
 * 
 * <pre>
 * File system search, pure java implementation.
 * Use Lucene to index/search files. And complete it with a basic java file browsing 
 * given the following algorithm :
 * At the step p, we'll search only the first n elements matching the given pattern.
 * We'll keep in memory where we have stop the search, in order to resume it at the step p+1.
 * For that, we'll consider folders that we have completely browsed (ie. with hierarchy), 
 * or just partially browsed (just files).
 * We'll add all browsed files to the index. That might produce duplicate entries that will
 * be purged from the index when closing the application.
 * 
 * Remark : a change in the files contained in a directory while the application is launched
 * will not be returned by the search.
 * Indeed, files browsed are not browsed a second time (using the _browsedHierarchyDirs attribute),
 * But are indexed, so returned by the search.
 * TODO: use Java 7 WatchService http://java.sun.com/developer/technicalArticles/javase/nio/#6
 * </pre>
 * 
 * @author Leo Lewis
 */
public class FileSystemSearchEngine extends SearchEngine {

	/** Folder to search */
	private File _searchDir;

	/** Already browsed folders (files and files of hierarchy) */
	private Set<String> _browsedHierarchyDirs;

	/** Already browsed folders (only the files of the folder) */
	private Set<String> _browsedDirs;

	/** Currently found entries */
	private Set<String> _currentResults;

	/** Search folder path text field */
	private JTextField _searchDirPathTextField;

	/** File search Index */
	private FileSearchIndex _fileSearchIndexer;

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public FileSystemSearchEngine() throws IOException {
		super();
		_browsedDirs = new HashSet<String>();
		_browsedHierarchyDirs = new HashSet<String>();
		_currentResults = new HashSet<String>();
		_fileSearchIndexer = new FileSearchIndex();
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#searchImpl(java.lang.String,
	 *      int, int, org.leo.oglexplorer.model.result.SearchType,
	 *      CancelMonitor)
	 */
	@Override
	protected List<? extends SearchResult> searchImpl(String words, int count, int offset, SearchType type,
			CancelMonitor cancelMonitor) {
		if (_searchDir == null) {
			return null;
		}
		List<FileSearchResult> results = new ArrayList<>();
		// search in indexed results
		List<File> indexedResults = _fileSearchIndexer.searchInIndex(words, offset + count);
		System.out.println("Found " + indexedResults.size() + " indexed results.");
		for (int i = offset; i < indexedResults.size(); i++) {
			results.add(createFileSearchResult(indexedResults.get(i), offset + i + 1));
		}

		// if it is not enough, search by browsing the directory
		if (indexedResults.size() < offset + count) {
			recSearch(_searchDir, words, count, _currentResults.size(), results, cancelMonitor);
		}
		return results;
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#initSearch(java.lang.String)
	 */
	@Override
	public void initSearch(String words) {
		super.initSearch(words);
		// _browsedDirs.clear();
		// _browsedHierarchyDirs.clear();
		_currentResults.clear();
	}

	/**
	 * Recursive search
	 * 
	 * @param dir folder to browse
	 * @param words words search
	 * @param max number of entries to search
	 * @param results list of results (to populate with new results)
	 * @param cancelMonitor monitor used by the algorithm to see if its needs to
	 *            stop because the user canceled the process
	 */
	private void recSearch(File dir, String words, int max, int offset, List<FileSearchResult> results,
			CancelMonitor cancelMonitor) {
		// if it's a file, a folder already entirely browsed or we have the
		// expected number of files: we exit
		if (cancelMonitor.isCanceled() || dir.isFile() || _currentResults.size() - offset >= max
				|| _browsedHierarchyDirs.contains(dir.getAbsolutePath())) {
			return;
		}
		File[] children = dir.listFiles();
		List<File> folders = new ArrayList<File>();
		// if we have already browsed the files of the folder, but not the
		// hierarchy
		boolean alreadyBrowsedFiles = _browsedDirs.contains(dir.getAbsolutePath());
		if (children != null) {
			// add the files to the index
			_fileSearchIndexer.addFilesToIndex(Arrays.asList(children));
			for (File child : children) {
				if (cancelMonitor.isCanceled()) {
					return;
				}
				// proceed files first
				if (child.isFile()) {
					// if the files of this folder have not been browsed and the
					// file name match the words
					if (!alreadyBrowsedFiles && child.getName().toLowerCase().contains(words)) {
						// check if it is really a new result, ie if we didn't
						// get it during a previous next search call
						if (!_currentResults.contains(child.getAbsolutePath())) {
							results.add(createFileSearchResult(child, _currentResults.size() + 1));
						}
						// we got our count, exit
						if (_currentResults.size() - offset >= max) {
							return;
						}
					}
				} else {
					// proceed later
					folders.add(child);
				}
			}
		}
		// files browsed
		_browsedDirs.add(dir.getAbsolutePath());
		// now proceed folder in alphabetical order
		Collections.sort(folders);
		for (File folder : folders) {
			recSearch(folder, words, max, offset, results, cancelMonitor);
		}
		// totally browse the folder
		_browsedHierarchyDirs.add(dir.getAbsolutePath());
	}

	/**
	 * Create a FileSearchResult from a File
	 * 
	 * @param file the file
	 * @param index index in the results
	 * @return the FileSearchResult
	 */
	private FileSearchResult createFileSearchResult(File file, int index) {
		FileSearchResult result = new FileSearchResult(index);
		result.setPath(file.getAbsolutePath());
		result.setTitle(file.getName());
		_currentResults.add(file.getAbsolutePath());
		return result;
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#getName()
	 */
	@Override
	public String getName() {
		return Resources.getLabel("engine.localdrive");
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#availableSearchTypes()
	 */
	@Override
	public SearchType[] availableSearchTypes() {
		return new SearchType[] { SearchType.FILE };
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#getLogo()
	 */
	@Override
	public Icon getLogo() {
		return Resources.getImageIcon("hard_drive.png");
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#additionalSearchComponent()
	 */
	@Override
	public JComponent additionalSearchComponent(final JPanel parent) {
		if (_searchDir == null) {
			_searchDir = new File(System.getProperty("user.home"));
		}
		JPanel comp = new JPanel(new BorderLayout());
		JButton chooseDirButton = new JButton(Resources.getImageIcon("folder.png"));
		comp.add(chooseDirButton, BorderLayout.WEST);

		chooseDirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseSearchDir(parent);
			}
		});
		_searchDirPathTextField = new JTextField(20);
		_searchDirPathTextField.setEditable(false);
		_searchDirPathTextField.setText(_searchDir.getAbsolutePath());
		_searchDirPathTextField.setToolTipText(_searchDir.getAbsolutePath());
		comp.add(_searchDirPathTextField);

		return comp;
	}

	/**
	 * Choose the search directory
	 */
	private void chooseSearchDir(JPanel parent) {
		JFileChooser fileChooser = new JFileChooser(_searchDir);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret = fileChooser.showOpenDialog(parent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File dir = fileChooser.getSelectedFile();
			if (dir != null) {
				_searchDir = dir;
				_searchDirPathTextField.setText(_searchDir.getAbsolutePath());
				_searchDirPathTextField.setToolTipText(_searchDirPathTextField.getText());
			}
		}
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#dispose()
	 */
	@Override
	public void dispose() {
		_fileSearchIndexer.dispose();
		super.dispose();
	}

	/**
	 * @see org.leo.oglexplorer.model.engine.SearchEngine#configPanel()
	 */
	@Override
	public CustomRunnable configPanel(Container parent) {
		JPanel lucenePanel = new JPanel();
		lucenePanel.add(new JLabel(Resources.getLabel("lucene.clear.index.cache")));
		JButton clearCache = new JButton(Resources.getImageIcon("bin.png"));
		clearCache.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					_fileSearchIndexer.clearIndex();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// need to rebrowse directories to rebuild the index
				_browsedDirs.clear();
				_browsedHierarchyDirs.clear();
			}
		});
		lucenePanel.add(clearCache);
		parent.add(lucenePanel);
		return null;
	}
}
