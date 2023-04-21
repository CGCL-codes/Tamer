/*-- 

 Copyright (C) 2000-2001 Anthony Eden.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "LogiTest" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact me@anthonyeden.com.
 
 4. Products derived from this software may not be called "LogiTest", nor
    may "LogiTest" appear in their name, without prior written permission
    from Anthony Eden (me@anthonyeden.com).
 
 In addition, I request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      Anthony Eden (http://www.anthonyeden.com/)."

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT, 
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.

 For more information on LogiTest, please see <http://www.logitest.org/>.
 
 */

package org.logitest.ui;

import java.util.Enumeration;
import javax.swing.tree.*;

import org.logitest.*;

/**	TreeModel for the TestTree class.

	@author Anthony Eden
*/

public class TestTreeModel extends DefaultTreeModel{

	/** Construct a new TestTreeModel.
	
		@param rootNode The root tree node
	*/

	public TestTreeModel(DefaultMutableTreeNode rootNode){
		super(rootNode);
	}

	/** Find the TreePath to the given user object or null if the user object
		is not found.
	
		@return The TreePath or null
		@param object The user object
	*/
	
	public TreePath findPath(Object object){
		TreeNode node = findNode(object);
		if(node != null){
			return new TreePath(getPathToRoot(node));
		} else {
			return null;
		}
	}

	/** Find the TreePath to a specified child of a resource.

		@param resource The resource that parents the node we are seeking the path of.
		@param childIndex The index of the resource's child we are seeking the path of
		@return The path to the resource child in the tree.
	*/
	public TreePath findResourceChildPath(Resource resource, int childIndex) {
		TreePath resourcePath = findPath(resource);
		Object[] resourcePathNodes = resourcePath.getPath();
		Object resourceNode = resourcePathNodes[resourcePathNodes.length - 1];
		TreeNode childNode = (TreeNode)getChild(resourceNode, childIndex);
		return new TreePath(getPathToRoot(childNode));
	}
	
	/** Find the TreeNode which contains the given user object or null if the 
		user object is not found.
	
		@return The TreeNode or null
		@param object The user object
	*/
	
	public TreeNode findNode(Object object){
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
		if(rootNode.getUserObject() == object){
			return rootNode;
		} else {
			Enumeration enum = rootNode.depthFirstEnumeration();
			while(enum.hasMoreElements()){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)enum.nextElement();
				if(node.getUserObject() == object){
					return node;
				}
			}
		}
		
		return null;
	}
	
}
