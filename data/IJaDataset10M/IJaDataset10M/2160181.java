package awilkins.eclipse.util;

import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaModelException;

/**
 *
 */
public interface IJavaModelVisitor {

    /**
   * Called with IJavaModel elements that cause true to be returned from {@link IJavaModelVisitor#matchesJavaModel}
   *
   * @param element
   * @throws JavaModelException
   */
    void visitMatchingJavaModel(IJavaModel element) throws JavaModelException;

    /**
   * Called with IJavaModel elements that cause false to be returned from {@link IJavaModelVisitor#matchesJavaModel}
   *
   * @param element
   * @throws JavaModelException
   */
    void visitNonMatchingJavaModel(IJavaModel element) throws JavaModelException;

    /**
   * Ascertain whether this IJavaModel element should be walked over. If this method returns true, then then
   * {@link IJavaModelVisitor#visitMatchingJavaModel}will be called, otherwise
   * {@link IJavaModelVisitor#visitNonMatchingJavaModel}will be called.
   *
   * @param element
   * @return true if the element is to be walked over
   * @throws JavaModelException
   */
    boolean matchesJavaModel(IJavaModel element) throws JavaModelException;
}
