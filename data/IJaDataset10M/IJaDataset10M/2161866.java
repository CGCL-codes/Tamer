package com.google.code.nanorm.test.resultmap;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.google.code.nanorm.Block;
import com.google.code.nanorm.ParamBlock;
import com.google.code.nanorm.SQLSource;
import com.google.code.nanorm.annotations.ResultMap;
import com.google.code.nanorm.annotations.ResultMapRef;
import com.google.code.nanorm.annotations.Select;
import com.google.code.nanorm.annotations.Source;
import com.google.code.nanorm.test.beans.Article;
import com.google.code.nanorm.test.common.MapperTestBase;

/**
 * 
 * @author Ivan Dubrov
 * @version 1.0 27.05.2008
 */
@SuppressWarnings("all")
public class TestDynamicSQL extends MapperTestBase {

    @ResultMap(id = "article", auto = true)
    public interface PublicationMapper {

        @ResultMapRef("article")
        @Select("SELECT id, subject, body, year FROM articles WHERE ID = ${1}")
        Article getArticleById(int id);

        @ResultMapRef("article")
        @Select("SELECT id, subject, body, year FROM articles ORDER BY id ASC")
        List<Article> listArticles();

        @Source(SelectSubjectYearSource.class)
        @ResultMapRef("article")
        List<Article> listBySubjectYear(String subject, int year);

        public static class SelectSubjectYearSource extends SQLSource {

            public void sql(final String subject, final int year) {
                append("SELECT id, subject, body, year FROM articles WHERE ");
                join(new Block() {

                    public void generate() {
                        if (subject != null) {
                            append(" subject = ${value} ", subject);
                        }
                    }
                }, new Block() {

                    public void generate() {
                        if (year != 0) {
                            append(" year = ${value} ", year);
                        }
                    }
                }).with(" AND ");
                append(" ORDER BY id ASC");
            }
        }

        @Source(SelectSubjectYearSource2.class)
        @ResultMapRef("article")
        List<Article> listBySubjectYear2(String[] subject, int year);

        public static class SelectSubjectYearSource2 extends SQLSource {

            public void sql(final String[] subject, final int year) {
                append("SELECT id, subject, body, year FROM articles WHERE ");
                join(new Block() {

                    public void generate() {
                        join(new ParamBlock<String>() {

                            public void generate(String subject) {
                                append(" subject = ${value} ", subject);
                            }
                        }, subject).open("(").close(")").with(" OR ");
                    }
                }, new Block() {

                    public void generate() {
                        if (year != 0) {
                            append(" year = ${value} ", year);
                        }
                    }
                }).with(" AND ");
                append(" ORDER BY id ASC");
            }
        }

        @Source(SelectByIdsSource.class)
        @ResultMapRef("article")
        List<Article> listByIds(Integer[] ids);

        public static class SelectByIdsSource extends SQLSource {

            public void sql(final Integer[] ids) {
                append("SELECT id, subject, body, year FROM articles ");
                join(new ParamBlock<Integer>() {

                    public void generate(Integer id) {
                        append(" id = ${value} ", id);
                    }
                }, ids).with(" OR ").open(" WHERE ");
                append(" ORDER BY id ASC");
            }
        }
    }

    /**
     * Test selecting the car by id.
     */
    @Test
    public void testSelectById() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        Article pub = mapper.getArticleById(2);
        Assert.assertEquals(2, pub.getId());
        Assert.assertEquals("Saving the Earth", pub.getSubject());
        Assert.assertEquals(2008, pub.getYear());
    }

    /**
     * Test selecting list of cars.
     */
    @Test
    public void testSelectList() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> cars = mapper.listArticles();
        Assert.assertEquals(2, cars.size());
        Article ar = cars.get(0);
        Assert.assertEquals(1, ar.getId());
        Assert.assertEquals("World Domination", ar.getSubject());
        Assert.assertEquals(2007, ar.getYear());
        ar = cars.get(1);
        Assert.assertEquals(2, ar.getId());
        Assert.assertEquals("Saving the Earth", ar.getSubject());
        Assert.assertEquals(2008, ar.getYear());
    }

    /**
     * Test listing by model.
     */
    @Test
    public void testListBySubjectYear() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> cars = mapper.listBySubjectYear("Saving the Earth", 0);
        Assert.assertEquals(1, cars.size());
        Article ar = cars.get(0);
        Assert.assertEquals(2, ar.getId());
    }

    /**
     * Test listing by model. Test joining empty list (the join should be
     * ignored completely).
     */
    @Test
    public void testListBySubjectYear2() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> cars = mapper.listBySubjectYear2(new String[0], 2007);
        Assert.assertEquals(1, cars.size());
        Article ar = cars.get(0);
        Assert.assertEquals(1, ar.getId());
    }

    /**
     * Test listing by year.
     */
    @Test
    public void testListBySubjectYear3() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> ars = mapper.listBySubjectYear(null, 2007);
        Assert.assertEquals(1, ars.size());
        Article ar = ars.get(0);
        Assert.assertEquals(1, ar.getId());
    }

    /**
     * Test listing both by model and year.
     */
    @Test
    public void testListByModelYear3() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> cars = mapper.listBySubjectYear("Test", 2006);
        Assert.assertEquals(0, cars.size());
    }

    /**
     * Test listing by list of ids
     */
    @Test
    public void testListByIds() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> articles = mapper.listByIds(new Integer[] { 1, 2 });
        Assert.assertEquals(2, articles.size());
    }

    /**
     * Test listing by list of ids
     */
    @Test
    public void testListByIds2() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> articles = mapper.listByIds(new Integer[] { 1 });
        Assert.assertEquals(1, articles.size());
    }

    /**
     * Test listing by list of ids
     */
    @Test
    public void testListByIds3() throws Exception {
        PublicationMapper mapper = factory.createMapper(PublicationMapper.class);
        List<Article> articles = mapper.listByIds(new Integer[0]);
        Assert.assertEquals(2, articles.size());
    }
}
