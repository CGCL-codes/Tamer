package org.easy.eao.spring.jpa;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.easy.eao.spring.jpa.model.Exam;
import org.easy.eao.support.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "context.xml", "paging-test.xml" })
public class PagingTest {

    @Resource
    private PagingEao eao;

    @Test
    public void test_hql() {
        Page<Exam> result = eao.hql("tom", 1, 1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        Exam exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(63));
        result = eao.hql("tom", 3, 1);
        assertThat(result.getNumber(), is(3));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(2));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(85));
    }

    @Test
    public void test_hql_without_count() {
        Page<Exam> result = eao.hql_without_count("tom", 1, 1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        Exam exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(63));
    }

    @Test
    public void test_named() {
        Page<Exam> result = eao.named("tom", 1, 1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        Exam exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(63));
        result = eao.named("tom", 3, 1);
        assertThat(result.getNumber(), is(3));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(2));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(85));
    }

    @Test
    public void test_named_without_count() {
        Page<Exam> result = eao.named_without_count("tom", 1, 1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(3));
        assertThat(result.getPageCount(), is(3));
        Exam exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(63));
    }

    @Test
    public void test_hql_named_para() {
        Set<String> subjects = new HashSet<String>();
        subjects.add("mandarin");
        subjects.add("math");
        Page<Exam> result = eao.hql_named_para("tom", subjects, 1, 1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(1));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(2));
        assertThat(result.getPageCount(), is(2));
        Exam exam = result.iterator().next();
        assertThat(exam.getStudent(), is("tom"));
        assertThat(exam.getGrade(), is(63));
    }

    @Test
    public void test_default_size() {
        Page<Exam> result = eao.default_size(1);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(3));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(9));
        assertThat(result.getPageCount(), is(3));
    }

    @Test
    public void test_default_page() {
        Page<Exam> result = eao.default_page(3);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(3));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(9));
        assertThat(result.getPageCount(), is(3));
    }

    @Test
    public void test_default_page_size() {
        Page<Exam> result = eao.default_page_size();
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(3));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(9));
        assertThat(result.getPageCount(), is(3));
    }

    @Test
    public void test_overwirte_default() {
        Page<Exam> result = eao.overwirte_default(1, 3);
        assertThat(result.getNumber(), is(1));
        assertThat(result.getSize(), is(3));
        assertThat(result.getStart(), is(0));
        assertThat(result.getCount(), is(9));
        assertThat(result.getPageCount(), is(3));
    }
}
