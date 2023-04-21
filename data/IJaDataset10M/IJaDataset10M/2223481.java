package com.br.fsm.projectdelivery.repositorio;

import java.util.ArrayList;
import android.database.Cursor;
import android.util.Log;
import com.br.fsm.projectdelivery.activity.R;
import com.br.fsm.projectdelivery.basica.Produto;
import com.br.fsm.projectdelivery.basica.Produto.Produtos;
import com.br.fsm.projectdelivery.excecao.RepositorioException;
import com.br.fsm.projectdelivery.util.Constantes;

public class RepositorioProduto extends RepositorioBasico implements IRepositorioProduto {

    private static RepositorioProduto instance;

    public static final String TABLE_NAME = "PRODUTO";

    public static RepositorioProduto getInstance() {
        if (instance == null) {
            instance = new RepositorioProduto();
        }
        return instance;
    }

    @Override
    public Produto getProdutoById(int id) throws RepositorioException {
        Cursor cursor = null;
        Produto produto = null;
        try {
            cursor = db.query(TABLE_NAME, Produto.getColumns(), Produtos.PRODUTO_ID + " = " + id, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                produto = new Produto();
                int idxId = cursor.getColumnIndex(Produtos.PRODUTO_ID);
                int idxPromocaoId = cursor.getColumnIndex(Produtos.PRODUTO_PROMOCAO_ID);
                int idxQuantidadeProduto = cursor.getColumnIndex(Produtos.PRODUTO_QTD);
                int idxDescricao = cursor.getColumnIndex(Produtos.PRODUTO_DESCRICAO);
                int idxValorUnitario = cursor.getColumnIndex(Produtos.PRODUTO_VALOR_UNITARIO);
                int idxUltimaAlteracao = cursor.getColumnIndex(Produtos.PRODUTO_ULTIMA_ALTERACAO);
                produto.setProdutoId(cursor.getString(idxId));
                produto.setPromocaoId(cursor.getString(idxPromocaoId));
                produto.setProdutoQuantidade(cursor.getString(idxQuantidadeProduto));
                produto.setProdutoDescricao(cursor.getString(idxDescricao));
                produto.setProdutoValorUnitario(cursor.getDouble(idxValorUnitario));
                produto.setProdutoUltimaAlteracao(cursor.getString(idxUltimaAlteracao));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constantes.CATEGORIA, e.getMessage());
            throw new RepositorioException(context.getResources().getString(R.string.db_error));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return produto;
    }

    @Override
    public ArrayList<Produto> getAllProduto() throws RepositorioException {
        Cursor cursor = null;
        ArrayList<Produto> produtos = null;
        try {
            produtos = new ArrayList<Produto>();
            cursor = db.query(TABLE_NAME, Produto.getColumns(), null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int idxId = cursor.getColumnIndex(Produtos.PRODUTO_ID);
                int idxPromocaoId = cursor.getColumnIndex(Produtos.PRODUTO_PROMOCAO_ID);
                int idxQuantidadeProduto = cursor.getColumnIndex(Produtos.PRODUTO_QTD);
                int idxDescricao = cursor.getColumnIndex(Produtos.PRODUTO_DESCRICAO);
                int idxValorUnitario = cursor.getColumnIndex(Produtos.PRODUTO_VALOR_UNITARIO);
                int idxUltimaAlteracao = cursor.getColumnIndex(Produtos.PRODUTO_ULTIMA_ALTERACAO);
                do {
                    Produto produto = new Produto();
                    produto.setProdutoId(cursor.getString(idxId));
                    produto.setPromocaoId(cursor.getString(idxPromocaoId));
                    produto.setProdutoQuantidade(cursor.getString(idxQuantidadeProduto));
                    produto.setProdutoDescricao(cursor.getString(idxDescricao));
                    produto.setProdutoValorUnitario(cursor.getDouble(idxValorUnitario));
                    produto.setProdutoUltimaAlteracao(cursor.getString(idxUltimaAlteracao));
                    produtos.add(produto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constantes.CATEGORIA, e.getMessage());
            throw new RepositorioException(context.getResources().getString(R.string.db_error));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return produtos;
    }

    @Override
    public void insertProduto(Produto produto) throws RepositorioException {
    }

    @Override
    public void updateProduto(Produto produto) throws RepositorioException {
    }

    @Override
    public void removeProduto(Produto produto) throws RepositorioException {
    }
}
