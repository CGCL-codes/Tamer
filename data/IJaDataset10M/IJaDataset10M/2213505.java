package com.dhev.constraints.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.dhev.ExpressionLanguageUtils;
import com.dhev.constraints.DecimalMinEL;

public class DecimalMinELValidatorTest {

    private DecimalMinELValidator validator = new DecimalMinELValidator();

    @Mock
    private DecimalMinEL decimalMinELAnnotation;

    @Mock
    private ExpressionLanguageUtils expressionLanguageUtils;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(expressionLanguageUtils.getDouble(Matchers.anyString())).thenReturn(2.5);
        validator.setExpressionLanguageUtils(expressionLanguageUtils);
        when(decimalMinELAnnotation.value()).thenReturn("#{testExpression}");
        when(decimalMinELAnnotation.includeLimit()).thenReturn(true);
        validator.initialize(decimalMinELAnnotation);
    }

    @Test
    public void isValidReturnsTrueIfParamIsNull() {
        assertTrue(validator.isValid(null));
    }

    @Test
    public void isValidReturnsFalseIfParamIsNotANumber() {
        assertFalse(validator.isValid("not a number"));
    }

    @Test
    public void isValidReturnsTrueIfParamIsGreaterThanMin() {
        assertTrue(validator.isValid(2.6));
    }

    @Test
    public void isValidReturnsFalseIfParamIsLessThanMin() {
        assertFalse(validator.isValid(2.4));
    }

    @Test
    public void isValidReturnsTrueIfParamIsEqualToMin() {
        assertTrue(validator.isValid(2.5));
    }

    @Test
    public void isValidReturnsFalseIfParamIsEqualToMinAndIncludeLimitIsFalse() {
        when(decimalMinELAnnotation.includeLimit()).thenReturn(false);
        validator.initialize(decimalMinELAnnotation);
        assertFalse(validator.isValid(2.5));
    }
}
