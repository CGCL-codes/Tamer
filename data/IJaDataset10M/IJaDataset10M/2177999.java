/**
 * JMdFile (Java Midori File) Copyright (C) 2001-2005 Midori IGA
 * (http://www003.upp.so-net.ne.jp/midori/midosoft.html)
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package jp.ne.nifty.iga.midori.file.xml;

import java.util.ArrayList;
import java.util.Collections;

import jp.ne.nifty.iga.midori.file.JMdFileItem;
import jp.ne.nifty.iga.midori.file.JMdFileItemSortByDate;
import jp.ne.nifty.iga.midori.file.JMdFileItemSortByRelativeDirectoryAndName;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JMdXmlTraverser {
	public static final boolean IS_DEBUG = false;

	public static boolean traverseSelect(Element eleRoot, ArrayList listFrom,
			ArrayList listTo) {
		NodeList nlistChild = eleRoot.getElementsByTagName("select");
		if (nlistChild == null) {
			System.out.println("selectタグを発見できず");
			return false;
		}

		for (int index = 0; index < nlistChild.getLength(); index++) {
			// ここで1個目のfile
			Node nodeLook = nlistChild.item(index);
			if (nodeLook.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element eleCurrentLook = (Element) nodeLook;
			traverseSelectChild(eleCurrentLook, listFrom, listTo);
		}

		return true;
	}

	public static boolean traverseSelectChild(Element eleRoot,
			ArrayList listFrom, ArrayList listTo) {
		NodeList nodelistItems = eleRoot.getChildNodes();

		for (int indexItem = 0; indexItem < nodelistItems.getLength(); indexItem++) {
			Node nodeItem = nodelistItems.item(indexItem);
			if (nodeItem.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element eleItem = (Element) nodeItem;
			if (eleItem.getTagName().equals("filename")) {
				// System.out.println("filename");
				traverseFilename(eleItem, listFrom, listTo);
			} else if (eleItem.getTagName().equals("add")) {
				traverseAdd(eleItem, listFrom, listTo);
			} else if (eleItem.getTagName().equals("remove")) {
				traverseRemove(eleItem, listFrom, listTo);
			} else if (eleItem.getTagName().equals("all")) {
				traverseAll(eleItem, listFrom, listTo);
			} else if (eleItem.getTagName().equals("sort")) {
				traverseSort(eleItem, listTo);
			} else if (eleItem.getTagName().equals("where")) {
				traverseWhere(eleItem, listTo);
			}
			// else if(eleCurrentLook.getTagName().equals("title"))
			// {
			// traverseTitle(eleCurrentLook);
			// }
			else {
				System.out.println("unknown command: [" + eleItem.getTagName()
						+ "]");
			}
		}
		return true;
	}

	protected static void traverseFilename(Element eleLook, ArrayList listFrom,
			ArrayList listTo) {
		NamedNodeMap attrs = eleLook.getAttributes();
		if (attrs == null) {
			return;
		}

		for (int iAttr = 0; iAttr < attrs.getLength(); iAttr++) {
			Attr attrLook = (Attr) attrs.item(iAttr);
			for (int index = 0; index < listFrom.size(); index++) {
				JMdFileItem fileLook = (JMdFileItem) listFrom.get(index);
				if (attrLook.getName().equals("match")) {
					if (fileLook.getName().indexOf(attrLook.getValue()) >= 0) {
						if (IS_DEBUG)
							System.out.println(attrLook.getValue() + "を含むもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("notMatch")) {
					if (fileLook.getName().indexOf(attrLook.getValue()) == (-1)) {
						if (IS_DEBUG)
							System.out.println(attrLook.getValue() + "を含まないもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("equals")) {
					if (attrLook.getValue().equals(fileLook.getName())) {
						if (IS_DEBUG)
							System.out.println(attrLook.getValue() + "と一致するもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("startsWith")) {
					if (fileLook.getName().startsWith(attrLook.getValue())) {
						if (IS_DEBUG)
							System.out.println(attrLook.getValue() + "で開始するもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("endsWith")) {
					if (fileLook.getName().endsWith(attrLook.getValue())) {
						if (IS_DEBUG)
							System.out.println(attrLook.getValue() + "で終了するもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("notStartsWith")) {
					if (fileLook.getName().startsWith(attrLook.getValue())) {
						if (IS_DEBUG)
							System.out
									.println(attrLook.getValue() + "で開始しないもの");
						listTo.add(fileLook);
					}
				} else if (attrLook.getName().equals("notEndsWith")) {
					if (fileLook.getName().endsWith(attrLook.getValue())) {
						if (IS_DEBUG)
							System.out
									.println(attrLook.getValue() + "で終了しないもの");
						listTo.add(fileLook);
					}
				} else {
					System.out.println("unknown command: ["
							+ attrLook.getName() + "]");
				}
			}
		}
	}

	protected static void traverseAll(Element eleLook, ArrayList listFrom,
			ArrayList listTo) {
		for (int index = 0; index < listFrom.size(); index++) {
			JMdFileItem fileLook = (JMdFileItem) listFrom.get(index);
			listTo.add(fileLook);
		}
	}

	protected static void traverseSort(Element eleLook, ArrayList listTo) {
		NamedNodeMap attrs = eleLook.getAttributes();
		if (attrs == null) {
			return;
		}

		String strOrder = "";
		boolean isDesc = false;
		for (int iAttr = 0; iAttr < attrs.getLength(); iAttr++) {
			Attr attrLook = (Attr) attrs.item(iAttr);
			if (attrLook.getName().equals("order")) {
				strOrder = attrLook.getValue();
			} else if (attrLook.getName().equals("desc")) {
				if (attrLook.getValue().equals("true")) {
					isDesc = true;
				}
			} else {
				System.out.println("unknown command: [" + attrLook.getName()
						+ "]");
			}
		}

		if (strOrder.equals("filename")) {
			if (IS_DEBUG)
				System.out.println("ディレクトリ付ファイル名でソートします");
			JMdFileItemSortByRelativeDirectoryAndName comp = new JMdFileItemSortByRelativeDirectoryAndName();
			comp.setDesc(isDesc);
			Collections.sort(listTo, comp);
		} else if (strOrder.equals("filedate")) {
			if (IS_DEBUG)
				System.out.println("ファイル日付でソートします");
			JMdFileItemSortByDate comp = new JMdFileItemSortByDate();
			comp.setDesc(isDesc);
			Collections.sort(listTo, comp);
		}
	}

	protected static void traverseWhere(Element eleLook, ArrayList listTo) {
		NamedNodeMap attrs = eleLook.getAttributes();
		if (attrs == null) {
			return;
		}

		String strKey = "";
		String strValue = "";
		long lCount = 1;
		for (int iAttr = 0; iAttr < attrs.getLength(); iAttr++) {
			Attr attrLook = (Attr) attrs.item(iAttr);
			if (attrLook.getName().equals("key")) {
				strKey = attrLook.getValue();
			} else if (attrLook.getName().equals("value")) {
				strValue = attrLook.getValue();
			} else if (attrLook.getName().equals("count")) {
				lCount = Long.parseLong(attrLook.getValue());
			} else {
				System.out.println("unknown command: [" + attrLook.getName()
						+ "]");
			}
		}

		if (strKey.equals("filedate")) {
			if (IS_DEBUG)
				System.out.println("ファイル日付で条件付けします");

			long lDividePoint = 0;
			if (strValue.equals("day")) {
				lDividePoint = System.currentTimeMillis() - lCount * 1000 * 60
						* 60 * 24;
			} else if (strValue.equals("week")) {
				lDividePoint = System.currentTimeMillis() - lCount * 7 * 1000
						* 60 * 60 * 24;
			} else if (strValue.equals("month")) {
				lDividePoint = System.currentTimeMillis() - lCount * 90 * 1000
						* 60 * 60 * 24;
			} else {
				System.out.println("unknown command: [" + strValue + "]");
			}

			// その後 集合から削除
			for (int index = listTo.size() - 1; index >= 0; index--) {
				JMdFileItem fileLook = (JMdFileItem) listTo.get(index);
				if (fileLook.getLastModified() < lDividePoint) {
					listTo.remove(index);
				}
			}
		}
	}

	protected static void traverseAdd(Element eleLook, ArrayList listFrom,
			ArrayList listTo) {
		if (IS_DEBUG)
			System.out.println("add");
		// 集合を作成
		ArrayList listWork = new ArrayList();
		traverseSelectChild(eleLook, listFrom, listWork);

		if (IS_DEBUG)
			System.out.println("add trace " + listWork.size() + "件");

		// その後 集合を追加
		for (int index = 0; index < listWork.size(); index++) {
			JMdFileItem fileLook = (JMdFileItem) listWork.get(index);
			// すでにあればスキップ
			if (listTo.contains(fileLook)) {
				continue;
			}
			listTo.add(fileLook);
		}
	}

	protected static void traverseRemove(Element eleLook, ArrayList listFrom,
			ArrayList listTo) {
		if (IS_DEBUG)
			System.out.println("remove");
		// 集合を作成
		ArrayList listWork = new ArrayList();
		traverseSelectChild(eleLook, listFrom, listWork);

		if (IS_DEBUG)
			System.out.println("remove trace " + listWork.size() + "件");

		// その後 集合を追加
		for (int index = 0; index < listWork.size(); index++) {
			JMdFileItem fileLook = (JMdFileItem) listWork.get(index);
			int iFind = listTo.indexOf(fileLook);
			if (iFind < 0) {
				continue;
			}
			listTo.remove(iFind);
		}
	}

}
