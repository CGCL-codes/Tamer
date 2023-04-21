package com.dashboard.service;

import com.dashboard.domain.Organizacao;

/**
 * @author jneves
 *
 */
public interface ServicoDre {

    /**
	 * criar um DRE novo (uo consolidado deverá ser criado neste momento)
	 * 
	 * @param organizacao a organiza&ccedil;&atilde;o a ser associada.
	 */
    public void criar(Organizacao organizacao, String nome, String descricao);
}
