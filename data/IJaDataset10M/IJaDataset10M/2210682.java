package br.com.guaraba.wally.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import br.com.guaraba.wally.android.R;
import br.com.guaraba.wally.android.domain.model.Anuncio;
import br.com.guaraba.wally.android.util.Ponto;
import br.com.guaraba.wally.android.util.PontoOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MapaActivity extends MapActivity {

    public static final String ANUNCIO = "MapaActivity_ANUNCIO";

    private Anuncio anuncio;

    private TextView lblTitulo;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.mapa);
        this.init();
    }

    private void init() {
        this.lblTitulo = (TextView) this.findViewById(R.id.mapa_titulo);
        final MapView mapa = (MapView) this.findViewById(R.id.mapa);
        mapa.setStreetView(true);
        final Intent intent = this.getIntent();
        if (intent != null) {
            final Object anuncio = intent.getSerializableExtra(ANUNCIO);
            if (anuncio != null) {
                this.anuncio = (Anuncio) anuncio;
                this.lblTitulo.setText(this.anuncio.getProduto().getNome() + " (" + this.anuncio.getValorFormatado() + ")");
                final GeoPoint ponto = new Ponto(this.anuncio.getLatitude(), this.anuncio.getLongitude());
                mapa.getOverlays().add(new PontoOverlay(ponto, R.drawable.guaraba_wally_30));
                mapa.getController().setZoom(18);
                mapa.getController().setCenter(ponto);
            }
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
