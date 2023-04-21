package org.meanbean.test;

import org.junit.Test;
import org.meanbean.lang.EquivalentFactory;
import org.meanbean.test.beans.BeanFactory;
import org.meanbean.test.beans.CounterDrivenEqualsBeanFactory;
import org.meanbean.test.beans.DifferentTypeAcceptingBeanFactory;
import org.meanbean.test.beans.FieldDrivenEqualsBean;
import org.meanbean.test.beans.FieldDrivenEqualsBeanFactory;
import org.meanbean.test.beans.NonReflexiveBeanFactory;
import org.meanbean.test.beans.NullAcceptingBeanFactory;
import org.meanbean.test.beans.NullEquivalentFactory;

public class EqualsMethodContractVerifierTest {

    private final EqualsMethodContractVerifier verifier = new EqualsMethodContractVerifier();

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsReflexiveShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsReflexive(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsReflexiveShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsReflexive(new NullEquivalentFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsReflexiveShouldThrowAssertionErrorWhenEqualsIsNotReflexive() throws Exception {
        verifier.verifyEqualsReflexive(new NonReflexiveBeanFactory());
    }

    @Test
    public void verifyEqualsReflexiveShouldNotThrowAssertionErrorWhenEqualsIsReflexive() throws Exception {
        verifier.verifyEqualsReflexive(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsSymmetricShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsSymmetric(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsSymmetricShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsSymmetric(new NullEquivalentFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsSymmetricShouldPreventTestingSymmetricOfNonEqualObjects() throws Exception {
        verifier.verifyEqualsSymmetric(new FieldDrivenEqualsBeanFactory(false));
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsSymmetricShouldThrowAssertionErrorWhenEqualsIsNotSymmetric() throws Exception {
        verifier.verifyEqualsSymmetric(new EquivalentFactory<FieldDrivenEqualsBean>() {

            private int counter;

            public FieldDrivenEqualsBean create() {
                return new FieldDrivenEqualsBean(counter++ != 1);
            }
        });
    }

    @Test
    public void verifyEqualsSymmetricShouldNotThrowAssertionErrorWhenEqualsIsSymmetric() throws Exception {
        verifier.verifyEqualsSymmetric(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsTransitiveShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsTransitive(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsTransitiveShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsTransitive(new NullEquivalentFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsTransitiveShouldPreventTestingTransitiveOfNonEqualObjects() throws Exception {
        verifier.verifyEqualsTransitive(new FieldDrivenEqualsBeanFactory(false));
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsTransitiveShouldThrowAssertionErrorWhenEqualsIsNotTransitive() throws Exception {
        verifier.verifyEqualsTransitive(new CounterDrivenEqualsBeanFactory(2));
    }

    @Test
    public void verifyEqualsTransitiveShouldNotThrowAssertionErrorWhenEqualsIsTransitive() throws Exception {
        verifier.verifyEqualsTransitive(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsConsistentShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsConsistent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsConsistentShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsConsistent(new NullEquivalentFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsConsistentShouldThrowAssertionErrorWhenEqualsIsInconsistent() throws Exception {
        verifier.verifyEqualsConsistent(new CounterDrivenEqualsBeanFactory(97));
    }

    @Test
    public void verifyEqualsConsistentShouldNotThrowAssertionErrorWhenEqualsIsConsistent() throws Exception {
        verifier.verifyEqualsConsistent(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsNullShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsNull(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsNullShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsNull(new NullEquivalentFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsNullShouldThrowAssertionErrorWhenEqualsIsTrueForNull() throws Exception {
        verifier.verifyEqualsNull(new NullAcceptingBeanFactory());
    }

    @Test
    public void verifyEqualsNullShouldNotThrowAssertionErrorWhenEqualsIsFalseForNull() throws Exception {
        verifier.verifyEqualsNull(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsDifferentTypeShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsDifferentType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsDifferentTypeShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsDifferentType(new NullEquivalentFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsDifferentTypeShouldThrowAssertionErrorWhenEqualsIsTrueForDifferentType() throws Exception {
        verifier.verifyEqualsDifferentType(new DifferentTypeAcceptingBeanFactory());
    }

    @Test
    public void verifyEqualsDifferentTypeShouldNotThrowAssertionErrorWhenEqualsIsFalseForDifferentType() throws Exception {
        verifier.verifyEqualsDifferentType(new BeanFactory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsShouldPreventNullFactory() throws Exception {
        verifier.verifyEqualsMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyEqualsShouldPreventFactoryThatCreatesNullObjects() throws Exception {
        verifier.verifyEqualsMethod(new NullEquivalentFactory());
    }

    @Test
    public void verifyEqualsShouldNotThrowAssertionErrorWhenEqualsIsCorrect() throws Exception {
        verifier.verifyEqualsMethod(new BeanFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsShouldThrowAssertionErrorWhenEqualsIsNotReflexive() throws Exception {
        verifier.verifyEqualsMethod(new NonReflexiveBeanFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsShouldThrowAssertionErrorWhenEqualsIsInconsistent() throws Exception {
        verifier.verifyEqualsMethod(new CounterDrivenEqualsBeanFactory(97));
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsShouldThrowAssertionErrorWhenEqualsIsTrueForNull() throws Exception {
        verifier.verifyEqualsMethod(new NullAcceptingBeanFactory());
    }

    @Test(expected = AssertionError.class)
    public void verifyEqualsShouldThrowAssertionErrorWhenEqualsIsTrueForDifferentType() throws Exception {
        verifier.verifyEqualsMethod(new DifferentTypeAcceptingBeanFactory());
    }
}
