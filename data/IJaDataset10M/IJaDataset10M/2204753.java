package net.sourceforge.javautil.sourcecode.java;

import net.sourceforge.javautil.sourcecode.java.IJavaSourcecodeVisitor.CommentType;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IJavaCommentable {

    void visitComment(CommentType type, String comment);
}
