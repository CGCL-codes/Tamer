package br.com.arsmachina.tapestrycrud.services;

import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;

/**
 * Service that provides {@link SingleTypeTreeService}s.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface TreeServiceSource {

    /**
	 * Returns the {@link SingleTypeTreeService} of a given type.
	 * 
	 * @param <T> a type.
	 * @param clasz a {@link Class}.
	 * @return an {@link SingleTypeTreeService}.
	 */
    <T> SingleTypeTreeService<T> get(Class<T> clasz);
}
