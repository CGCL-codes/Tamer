package br.com.geostore.adapters;

import java.util.List;
import br.com.geostore.activities.R;
import br.com.geostore.entity.Voucher;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListaVouchersAdapter extends BaseAdapter {

    private static final String TAG = "ListaVouchersAdapter";

    private Context ctx;

    private List<Voucher> vouchers;

    public ListaVouchersAdapter(Context ctx, List<Voucher> vouchers) {
        this.ctx = ctx;
        this.vouchers = vouchers;
    }

    @Override
    public int getCount() {
        return vouchers.size();
    }

    @Override
    public Object getItem(int position) {
        return vouchers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Voucher voucher = this.vouchers.get(position);
        View v = LayoutInflater.from(ctx).inflate(R.layout.meusvouchers, null);
        TextView tvNomeProd = (TextView) v.findViewById(R.id.tvNomeProdMeusVouchers);
        TextView tvDescProd = (TextView) v.findViewById(R.id.tvDescProdMeusVouchers);
        TextView tvNomeLoja = (TextView) v.findViewById(R.id.tvNomeLojaMeusVouchers);
        TextView tvEndLoja = (TextView) v.findViewById(R.id.tvEndLojaMeusVouchers);
        TextView tvNumLoja = (TextView) v.findViewById(R.id.tvNumLojaMeusVouchers);
        TextView tvBairroLoja = (TextView) v.findViewById(R.id.tvBairroMeusVouchers);
        TextView tvPrcProd = (TextView) v.findViewById(R.id.tvPrcProdMeusVouchers);
        TextView tvNumVoucher = (TextView) v.findViewById(R.id.tvNumVoucherMeusVouchers);
        TextView tvDescPromo = (TextView) v.findViewById(R.id.tvDescPromoMeusVouchers);
        tvNomeProd.setText(voucher.getPromocao().getProduto().getNome());
        tvDescProd.setText("Desc: ".concat(voucher.getPromocao().getProduto().getDescricao()));
        tvNomeLoja.setText("Loja: ".concat(voucher.getPromocao().getProduto().getLoja().getNomeFantasia()));
        tvEndLoja.setText("End: ".concat(voucher.getPromocao().getProduto().getLoja().getEndereco().getLogradouro()));
        tvNumLoja.setText("Num: ".concat(voucher.getPromocao().getProduto().getLoja().getEndereco().getNumeroLogradouro()));
        tvBairroLoja.setText("Bairro: ".concat(voucher.getPromocao().getProduto().getLoja().getEndereco().getBairro()));
        tvPrcProd.setText("Pre�o: ".concat(voucher.getPromocao().getProduto().getValor().toString()));
        tvNumVoucher.setText("Voucher: ".concat(voucher.getCodigoVoucher()));
        tvDescPromo.setText("Promo��o: ".concat(voucher.getPromocao().getDescricao()));
        return v;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
