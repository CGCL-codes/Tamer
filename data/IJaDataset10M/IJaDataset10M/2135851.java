package br.com.linkcom.neo.persistence;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import br.com.linkcom.neo.controller.crud.FiltroListagem;

/**
 * Cria um resultSet para ser usado na listagem
 * Atualiza o filtroListagem com o n�mero de paginas e a p�gina atual
 */
public class ListagemResult<E> {

    protected List<E> lista = null;

    @SuppressWarnings("unchecked")
    public ListagemResult(QueryBuilder<E> queryBuilder, FiltroListagem filtroListagem) {
        init(queryBuilder, filtroListagem);
    }

    protected void init(QueryBuilder<E> queryBuilder, FiltroListagem filtroListagem) {
        QueryBuilder<Integer> countQueryBuilder = new QueryBuilder<Integer>(queryBuilder.getHibernateTemplate());
        String select = queryBuilder.getSelect().getValue().trim();
        if (!select.toLowerCase().startsWith("distinct")) {
            countQueryBuilder.select("count(*)");
        } else {
            countQueryBuilder.select("count(" + select + ")");
        }
        QueryBuilder.From from = queryBuilder.getFrom();
        countQueryBuilder.from(from);
        List<QueryBuilder<E>.Join<E>> joins = queryBuilder.getJoins();
        for (QueryBuilder.Join join : joins) {
            countQueryBuilder.join(join.getJoinMode(), false, join.getPath());
        }
        QueryBuilder.Where where = queryBuilder.getWhere();
        countQueryBuilder.where(where);
        Integer numeroResultados = countQueryBuilder.unique();
        filtroListagem.setNumberOfResults(numeroResultados.intValue());
        if (filtroListagem.getEVENT().equals(FiltroListagem.FILTER)) {
            filtroListagem.setCurrentPage(0);
        }
        int i = numeroResultados.intValue() / filtroListagem.getPageSize();
        if (numeroResultados.intValue() % filtroListagem.getPageSize() != 0) {
            i++;
        }
        filtroListagem.setNumberOfPages(i);
        queryBuilder.setPageNumberAndSize(filtroListagem.getCurrentPage(), filtroListagem.getPageSize());
        if (!StringUtils.isEmpty(filtroListagem.getOrderBy())) {
            queryBuilder.orderBy(filtroListagem.getOrderBy() + " " + (filtroListagem.isAsc() ? "ASC" : "DESC"));
        }
        lista = queryBuilder.list();
    }

    public List<E> list() {
        return lista;
    }
}
