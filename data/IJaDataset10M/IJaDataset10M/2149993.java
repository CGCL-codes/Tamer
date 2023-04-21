package com.kmlitro.entity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.kmlitro.entity.MarcaModelo;
import com.kmlitro.entity.Veiculo;
import com.kmlitro.entity.dao.db.DBManager;

public class VeiculoDAO {

    public void createVeiculo(Veiculo veiculo) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "INSERT INTO TB_KML_CARRO(KML_ANO_CARRO, KML_FOTO_CARRO, KML_HODOMETRO_CARRO, KML_ID_MARCA_CARRO, KML_PLACA_CARRO, KML_ID_MODELO_CARRO, KML_ID_USUARIO_CARRO, KML_TIPO_VEICULO) VALUES (?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, veiculo.getAno());
            pstmt.setString(2, veiculo.getFotoCarro());
            pstmt.setString(3, veiculo.getHodometro());
            pstmt.setString(4, veiculo.getMarca());
            pstmt.setString(5, veiculo.getPlaca());
            pstmt.setString(6, veiculo.getModelo());
            pstmt.setInt(7, veiculo.getIdUsuario());
            pstmt.setInt(8, veiculo.getTipoVeiculo());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }

    public List<Veiculo> selectVeiculos(int userId) throws Exception {
        List<Veiculo> veiculos = new ArrayList<Veiculo>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "SELECT * FROM TB_KML_CARRO WHERE KML_ID_USUARIO_CARRO = ? ORDER BY KML_ID_CARRO DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (null != rs && rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setAno(rs.getInt("KML_ANO_CARRO"));
                veiculo.setFotoCarro(rs.getString("KML_FOTO_CARRO"));
                veiculo.setHodometro(rs.getString("KML_HODOMETRO_CARRO"));
                veiculo.setId(rs.getInt("KML_ID_CARRO"));
                veiculo.setMarca(rs.getString("KML_ID_MARCA_CARRO"));
                veiculo.setModelo(rs.getString("KML_ID_MODELO_CARRO"));
                veiculo.setIdUsuario(rs.getInt("KML_ID_USUARIO_CARRO"));
                veiculo.setTipoVeiculo(rs.getInt("KML_TIPO_VEICULO"));
                veiculo.setPlaca(rs.getString("KML_PLACA_CARRO"));
                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new Exception(e);
            }
        }
        return veiculos;
    }

    public Veiculo selectVeiculobyId(int veiculoId) throws Exception {
        Veiculo veiculo = new Veiculo();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "SELECT * FROM TB_KML_CARRO WHERE KML_ID_CARRO = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, veiculoId);
            rs = pstmt.executeQuery();
            if (null != rs && rs.next()) {
                veiculo.setAno(rs.getInt("KML_ANO_CARRO"));
                veiculo.setFotoCarro(rs.getString("KML_FOTO_CARRO"));
                veiculo.setHodometro(rs.getString("KML_HODOMETRO_CARRO"));
                veiculo.setId(rs.getInt("KML_ID_CARRO"));
                veiculo.setMarca(rs.getString("KML_ID_MARCA_CARRO"));
                veiculo.setModelo(rs.getString("KML_ID_MODELO_CARRO"));
                veiculo.setIdUsuario(rs.getInt("KML_ID_USUARIO_CARRO"));
                veiculo.setTipoVeiculo(rs.getInt("KML_TIPO_VEICULO"));
                veiculo.setPlaca(rs.getString("KML_PLACA_CARRO"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new Exception(e);
            }
        }
        return veiculo;
    }

    public void deleteVeiculo(int idVeiculo, int userId) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "DELETE FROM TB_KML_CARRO WHERE KML_ID_CARRO = ? AND KML_ID_USUARIO_CARRO = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVeiculo);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }

    public void updateHodometro(int veiculoId, String hodometro) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "UPDATE TB_KML_CARRO SET " + "KML_HODOMETRO_CARRO = ? " + "WHERE KML_ID_CARRO = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hodometro);
            pstmt.setInt(2, veiculoId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }

    public void updateImage(int veiculoId, String image) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "UPDATE TB_KML_CARRO SET " + "KML_FOTO_CARRO = ? " + "WHERE KML_ID_CARRO = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, image);
            pstmt.setInt(2, veiculoId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }

    public void editVeiculo(Veiculo veiculo) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "UPDATE TB_KML_CARRO SET " + "KML_ANO_CARRO = ?, " + "KML_FOTO_CARRO = ?, " + "KML_HODOMETRO_CARRO = ?, " + "KML_ID_MARCA_CARRO = ?, " + "KML_PLACA_CARRO = ?, " + "KML_ID_MODELO_CARRO = ?, " + "KML_TIPO_VEICULO = ? " + "WHERE KML_ID_CARRO = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, veiculo.getAno());
            pstmt.setString(2, veiculo.getFotoCarro());
            pstmt.setString(3, veiculo.getHodometro());
            pstmt.setString(4, veiculo.getMarca());
            pstmt.setString(5, veiculo.getPlaca());
            pstmt.setString(6, veiculo.getModelo());
            pstmt.setInt(7, veiculo.getTipoVeiculo());
            pstmt.setInt(8, veiculo.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }

    public List<MarcaModelo> selectMarcas() throws Exception {
        List<MarcaModelo> marcaModelo = new ArrayList<MarcaModelo>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "SELECT DISTINCT KML_MARCA_CARRO FROM TB_KML_MARCA_CARRO";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (null != rs && rs.next()) {
                MarcaModelo marca = new MarcaModelo();
                marca.setMarca(rs.getString("KML_MARCA_CARRO"));
                marcaModelo.add(marca);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new Exception(e);
            }
        }
        return marcaModelo;
    }

    public List<MarcaModelo> selectModelos() throws Exception {
        List<MarcaModelo> marcaModelo = new ArrayList<MarcaModelo>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getInstance().getConnectionMysql();
            String sql = "SELECT DISTINCT KML_MODELO_CARRO FROM TB_KML_MARCA_CARRO ORDER BY KML_MODELO_CARRO";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (null != rs && rs.next()) {
                MarcaModelo marca = new MarcaModelo();
                marca.setModelo(rs.getString("KML_MODELO_CARRO"));
                marcaModelo.add(marca);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new Exception(e);
            }
        }
        return marcaModelo;
    }
}
