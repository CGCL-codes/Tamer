package br.com.beyondclick.model.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import br.com.beyondclick.model.entity.Cheque;
import br.com.beyondclick.model.entity.Compra;
import br.com.beyondclick.model.entity.CompraMaterial;
import br.com.beyondclick.model.entity.CompraPedidoMaterial;
import br.com.beyondclick.model.entity.Material;
import br.com.beyondclick.model.entity.PedidoMaterial;
import br.com.gentech.commons.model.dao.DAOException;
import br.com.gentech.commons.model.entity.BusinessException;
import br.com.gentech.commons.model.repository.Repository;
import br.com.gentech.commons.model.repository.RepositoryBean;

@Stateless
public class CompraRepositoryBean extends RepositoryBean<Compra, Long> implements CompraRepository {

    @EJB(beanName = "RepositoryBean")
    private Repository<Cheque, Long> chequeRepository;

    @Override
    public Compra save(Compra compra) throws BusinessException {
        Cheque cheque = chequeRepository.find(Cheque.class, compra.getCheque().getId());
        compra.setCheque(cheque);
        cheque.debitar(compra.getTotalCusto());
        compra.atenderPedidosPendentes(findPedidoMaterialPendetes());
        chequeRepository.save(compra.getCheque());
        atualizarQuantidadeCompradaEm(compra);
        return super.save(compra);
    }

    /**
	 * Atualizar a quantidade comprada dos {@link PedidoMaterial} envolvidos na {@link Compra}.
	 * @param unsaved
	 */
    private void atualizarQuantidadeCompradaEm(Compra unsaved) {
        try {
            List<CompraPedidoMaterial> lstCompraPedidoMaterial = new ArrayList<CompraPedidoMaterial>();
            List<PedidoMaterial> pedidosMateriaisPendetes = findPedidoMaterialPendetes();
            for (PedidoMaterial pedidoMaterial : pedidosMateriaisPendetes) {
                for (CompraMaterial compraMaterial : unsaved.getCompraMateriais()) {
                    if (compraMaterial.getMaterial().equals(pedidoMaterial.getMaterial())) {
                        lstCompraPedidoMaterial.add(new CompraPedidoMaterial(compraMaterial, pedidoMaterial, compraMaterial.getQuantidade()));
                    }
                }
            }
            for (CompraPedidoMaterial compraPedidoMaterial : lstCompraPedidoMaterial) {
                getEntityManager().merge(compraPedidoMaterial.getPedidoMaterial());
            }
        } catch (NullPointerException e) {
            throw new DAOException("Lista de PedidoMaterial est� nula.", e);
        }
    }

    /**
	 * FIXME Atulizar os valores comprados, retirando os que foram deletados.
	 * 
	 * @throws BusinessException 
	 */
    @Override
    public void remove(Compra compra) throws BusinessException {
        Cheque cheque = chequeRepository.find(Cheque.class, compra.getCheque().getId());
        compra.setCheque(cheque);
        cheque.creditar(compra.getTotalCusto());
        compra.atenderPedidosPendentes(findPedidoMaterialPendetes());
        chequeRepository.save(compra.getCheque());
        super.remove(Compra.class, compra.getOID());
    }

    @SuppressWarnings("unchecked")
    public List<Compra> find(Date inicio, Date fim) {
        StringBuilder hql = new StringBuilder();
        hql.append("select c ");
        hql.append("from Compra c ");
        hql.append("where c.data >= :inicio and c.data <= :fim ");
        Query query = getEntityManager().createQuery(hql.toString());
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);
        Set<Compra> compras = new LinkedHashSet<Compra>(query.getResultList());
        return new ArrayList<Compra>(compras);
    }

    public Map<Material, Long> findMateriaisPendentesCompra() {
        Map<Material, Long> materialPedido = new HashMap<Material, Long>();
        Map<Material, Long> materialComprado = new HashMap<Material, Long>();
        Map<Material, Long> materialPendente = new HashMap<Material, Long>();
        List<PedidoMaterial> pedidoMateriaisPendentes = findPedidoMaterialPendetes();
        long quantidadePendente = 0;
        Material material;
        for (PedidoMaterial pedidoMaterial : pedidoMateriaisPendentes) {
            material = pedidoMaterial.getMaterial();
            long quantidadePedida = pedidoMaterial.getQuantidade();
            if (materialPedido.containsKey(material)) {
                quantidadePedida += materialPedido.get(material);
            }
            materialPedido.put(material, quantidadePedida);
            long quantidadeComprada = 0;
            final List<CompraPedidoMaterial> compraPedidoMateriais = pedidoMaterial.getCompraPedidoMateriais();
            for (CompraPedidoMaterial compraPedidoMaterial : compraPedidoMateriais) {
                quantidadeComprada += compraPedidoMaterial.getQuantidade();
            }
            if (materialComprado.containsKey(material)) {
                quantidadeComprada += materialComprado.get(material);
            } else {
                materialComprado.put(material, quantidadeComprada);
            }
            quantidadePendente = quantidadePedida - quantidadeComprada;
            materialPendente.put(material, quantidadePendente);
            quantidadePedida = 0;
            quantidadePendente = 0;
            quantidadeComprada = 0;
        }
        return materialPendente;
    }

    @SuppressWarnings("unchecked")
    public List<PedidoMaterial> findPedidoMaterialPendetes() {
        StringBuilder hql = new StringBuilder();
        hql.append("select ");
        hql.append("	pm ");
        hql.append("from PedidoMaterial pm ");
        hql.append("	left join fetch pm.compraPedidoMateriais ");
        hql.append("where ");
        hql.append("	pm not in( ");
        hql.append("		select cpm_naocomprado.pedidoMaterial ");
        hql.append("		from CompraPedidoMaterial cpm_naocomprado ");
        hql.append("	) ");
        hql.append("or	pm.quantidade > ( ");
        hql.append("		select sum(cpm_parcial.quantidade) ");
        hql.append("		from CompraPedidoMaterial cpm_parcial ");
        hql.append("		where cpm_parcial.pedidoMaterial = pm ");
        hql.append("	) ");
        hql.append("order by ");
        hql.append("	pm.pedido.data ");
        Query query = getEntityManager().createQuery(hql.toString());
        Set<PedidoMaterial> setPedidoMaterial = new HashSet<PedidoMaterial>(query.getResultList());
        List<PedidoMaterial> pedidoMaterial = new ArrayList<PedidoMaterial>(setPedidoMaterial);
        return pedidoMaterial;
    }

    /**
	 * FIXME Falta mostrar o quanto est� sobrando, ao inv�s do quanto foi comprado.
select
    new map(
		cm
		, (cm.quantidade - sum(cpm.quantidade)) as emEstoque
	)
from
	CompraMaterial cm
	inner join cm.compraPedidoMateriais cpm
where
	cm.	quantidade > (
		select
			sum(cpmUsados.quantidade)
		from
			CompraPedidoMaterial cpmUsados
		where
			cpmUsados.compraMaterial = cm
	)
group by
	cm.id
	, cpm.quantidade
	, cm.quantidade
	, cm.material
	, cm.preco
	, cm.compra
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Map<CompraMaterial, Long> findCompraMaterialEmEstoque() {
        StringBuilder hql = new StringBuilder();
        hql.append("select ");
        hql.append("	new map( ");
        hql.append("		cm as compraMaterial ");
        hql.append("		, (cm.quantidade - sum(cpm.quantidade)) as emEstoque ");
        hql.append("	) ");
        hql.append("from ");
        hql.append("	CompraMaterial cm ");
        hql.append("	inner join cm.compraPedidoMateriais cpm ");
        hql.append("where ");
        hql.append("	cm.	quantidade > ( ");
        hql.append("		select ");
        hql.append("			sum(cpmUsados.quantidade) ");
        hql.append("		from ");
        hql.append("			CompraPedidoMaterial cpmUsados ");
        hql.append("		where ");
        hql.append("			cpmUsados.compraMaterial = cm ");
        hql.append("	) ");
        hql.append("group by ");
        hql.append("	cm.id ");
        hql.append("	, cpm.quantidade ");
        hql.append("	, cm.quantidade ");
        hql.append("	, cm.material ");
        hql.append("	, cm.preco ");
        hql.append("	, cm.compra ");
        Query query = getEntityManager().createQuery(hql.toString());
        Map<CompraMaterial, Long> compraMaterialEmEstoque = new HashMap<CompraMaterial, Long>();
        List<Map> resultado = query.getResultList();
        for (Map object : resultado) {
            compraMaterialEmEstoque.put((CompraMaterial) object.get("compraMaterial"), (Long) object.get("emEstoque"));
        }
        return compraMaterialEmEstoque;
    }

    /**
	 * @see br.com.beyondclick.model.dao.CompraDAO#find(br.com.beyondclick.model.entity.Cheque)
	 */
    @SuppressWarnings("unchecked")
    public List<CompraPedidoMaterial> find(Cheque cheque) {
        List<CompraPedidoMaterial> compraPedidoMaterial = new ArrayList<CompraPedidoMaterial>();
        StringBuilder hql = new StringBuilder();
        hql.append("from Compra c ");
        hql.append("left join fetch c.compraMateriais cm ");
        hql.append("where c.cheque = :cheque ");
        hql.append("order by c.data ");
        Query query = getEntityManager().createQuery(hql.toString());
        query.setParameter("cheque", cheque);
        Set<Compra> compras = new LinkedHashSet<Compra>(query.getResultList());
        for (Compra compra : compras) {
            for (CompraMaterial compraMaterial : compra.getCompraMateriais()) {
                hql = new StringBuilder();
                hql.append("from CompraMaterial cm ");
                hql.append("left join fetch cm.compraPedidoMateriais cpm ");
                hql.append("left join fetch cpm.pedidoMaterial pm ");
                hql.append("left join fetch pm.pedido p ");
                hql.append("left join fetch p.orgao ");
                hql.append("left join fetch cpm.compraMaterial cm ");
                hql.append("where cm = :compraMaterial ");
                hql.append("order by p.orgao, pm.material ");
                query = getEntityManager().createQuery(hql.toString());
                query.setParameter("compraMaterial", compraMaterial);
                List<CompraMaterial> cm = query.getResultList();
                for (CompraPedidoMaterial pedidoMaterial : cm.get(0).getCompraPedidoMateriais()) {
                    pedidoMaterial.getPedidoMaterial().setCompraPedidoMateriais(cm.get(0).getCompraPedidoMateriais());
                    pedidoMaterial.getCompraMaterial().setCompra(compra);
                    compraPedidoMaterial.add(pedidoMaterial);
                }
            }
        }
        Collections.sort(compraPedidoMaterial);
        return compraPedidoMaterial;
    }

    /**
	 * Filtra os cheques usados em compras.
	 */
    @SuppressWarnings("unchecked")
    public List<Cheque> findCheque(Date inicio, Date fim) {
        StringBuilder hql = new StringBuilder();
        hql.append("select c.cheque ");
        hql.append("from Compra c ");
        hql.append("where c.data between :inicio and :fim ");
        hql.append("order by c.cheque.numero ");
        Query query = getEntityManager().createQuery(hql.toString());
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);
        Set<Cheque> cheques = new LinkedHashSet<Cheque>(query.getResultList());
        return new ArrayList<Cheque>(cheques);
    }
}
